package de.dontletyoudie.backend.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dontletyoudie.backend.security.CachedBodyHttpServletRequest;
import de.dontletyoudie.backend.security.tokenservice.TokenService;
import de.dontletyoudie.backend.security.tokenservice.TokenServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class DDAuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final Map<String, Method> filterMethods = new HashMap<>();
    private final Map<String, Method> filterMethodsWithToken = new HashMap<>();

    public DDAuthorizationFilter() {
        this.tokenService = TokenServiceFactory.getTokeService();
        findFilter();
    }

    private void findFilter() {
        AnnotatedTypeScanner scanner = new AnnotatedTypeScanner(Filter.class);
        Set<Class<?>> types = scanner.findTypes("de.dontletyoudie.backend");

        for (Class<?> c : types) {
            Class<?> c1;
            try {
                c1 = Class.forName(c.getName());
            } catch (ClassNotFoundException e) {
                continue;
            }

            for (Method m : c1.getDeclaredMethods()) {
                if (! m.isAnnotationPresent(PathFilter.class)) continue;

                String[] path = m.getAnnotation(PathFilter.class).path();
                Map<String, Method> map =
                        m.getAnnotation(PathFilter.class).tokenRequired() ? filterMethodsWithToken : filterMethods;

                Arrays.stream(path).forEach(p -> {
                    if (map.containsKey(p))
                        throw new IllegalStateException("Multiple @PathFilter with same path and tokenRequired");
                    map.put(p, m);
                });
            }
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        request.getParameterMap();
        HttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

        FilterData.Builder filterDataBuilder = new FilterData.Builder()
                .withRequest(cachedRequest)
                .withFilterChain(filterChain)
                .withFilterMethods(filterMethods)
                .withResponse(response);

        try {
            if (findAndDoPathFilter(filterDataBuilder.build())) return;
        } catch (Exception e) {
            e.printStackTrace();
            respondError(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        Optional<DecodedJWT> decodedTokenOptional = tryDecodeToken(cachedRequest, response);
        if (decodedTokenOptional.isEmpty()){
            return;
        }
        DecodedJWT decodedToken = decodedTokenOptional.get();

        filterDataBuilder
                .withFilterMethods(filterMethodsWithToken)
                .withToken(decodedToken);

        try {
            if (findAndDoPathFilter(filterDataBuilder.build())) return;
        } catch (Exception e) {
            respondError(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        List<String> claims = decodedToken.getClaim("roles").asList(String.class);
        if (claims == null) {
            respondError(response, "Token is invalid (not expired)", HttpStatus.UNAUTHORIZED);
            return;
        }

        List<SimpleGrantedAuthority> roles = claims.stream().map(SimpleGrantedAuthority::new).toList();

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(decodedToken.getSubject(), null, roles);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(cachedRequest, response);
        } catch (Exception e) {
            respondError(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean findAndDoPathFilter(FilterData data)
            throws InvocationTargetException, IOException, ServletException, IllegalAccessException {
        System.out.println(data);
        Method method = data.getFilterMethods().get(data.getRequest().getServletPath());
        if (method == null) return false;

        PathFilterResult pathFilterResult = (PathFilterResult) method.invoke(null, data);

        if (pathFilterResult.isInstantGrant()) {
            data.getFilterChain().doFilter(data.getRequest(), data.getResponse());
            return true;
        }

        if (pathFilterResult.isAccessDenied()) {
            respondError(data.getResponse(), pathFilterResult.getMessage(), HttpStatus.FORBIDDEN);
            return true;
        }
        return false;
    }

    private Optional<DecodedJWT> tryDecodeToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Optional<DecodedJWT> decodedTokenOptional;
        try {
            decodedTokenOptional =
                    tokenService.extractAndDecodeJWT(request.getHeader(HttpHeaders.AUTHORIZATION));
        } catch (ResponseStatusException e) {
            respondError(response, e.getReason(), e.getStatus());
            return Optional.empty();
        }
        return decodedTokenOptional;
    }

    private static void respondError(HttpServletResponse response, String errorMessage, HttpStatus status)
            throws IOException {
        response.setStatus(status.value());

        Map<String, String> error = new HashMap<>();
        error.put("error", errorMessage);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
