package de.dontletyoudie.backend.security.tokenservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
class DefaultTokenService implements TokenService {

    @Override
    public TokenDto genAccessAndRefreshToken(String username, HttpServletRequest request, List<String> roles) {
        return new TokenDto(genAccessToken(username, request, roles), genRefreshToken(username, request));
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
        } catch (TokenExpiredException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token expired");
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
