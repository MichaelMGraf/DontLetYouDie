package de.dontletyoudie.backend.security.tokenservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
class DefaultTokenService implements TokenService {

    @Override
    public Map<String, String> genAccessAndRefreshToken(String username, HttpServletRequest request, List<String> roles) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", genAccessToken(username, request, roles));
        tokens.put("refresh_token", genRefreshToken(username, request));
        return tokens;
    }

    @Override
    public String genRefreshToken(String username, HttpServletRequest request) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + TokenStatics.REFRESH_TOKEN_TIME))
                .withIssuer(request.getRequestURL().toString())
                .sign(TokenStatics.getAlgorithm());
    }

    @Override
    public String genAccessToken(String username, HttpServletRequest request, List<String> roles) {
        //List<String> list = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + TokenStatics.ACCESS_TOKEN_TIME))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", roles)
                .sign(TokenStatics.getAlgorithm());
    }

    @Override
    public Optional<DecodedJWT> extractAndDecodeJWT(String token) {
        if (token == null || token.length() <= TokenStatics.BEARER.length()
                || !token.startsWith(TokenStatics.BEARER))
            return Optional.empty();

        try {
            return Optional.of(
                    JWT.require(TokenStatics.getAlgorithm())
                            .build()
                            .verify(token.substring(
                                            TokenStatics.BEARER.length()
                                    )
                            )
            );
        } catch (JWTDecodeException e) {
            if (e.getMessage().equals("The token was expected to have 3 parts, but got 0."))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token was expected to have 3 parts");
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token encoding is invalid");
        } catch (SignatureVerificationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is invalid (not expired)");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
