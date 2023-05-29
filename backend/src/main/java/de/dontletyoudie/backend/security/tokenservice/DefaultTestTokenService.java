package de.dontletyoudie.backend.security.tokenservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;


//@Service
class DefaultTestTokenService extends DefaultTokenService implements TestTokenService {

    private final String url = "http://localhost:8080/login";

    @Override
    public TokenDto genAccessAndRefreshToken(String username, List<String> roles) {
        return new TokenDto(genAccessToken(username, roles), genRefreshToken(username));
    }

    @Override
    public String genRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + TokenStatics.REFRESH_TOKEN_TIME))
                .withIssuer(url)
                .sign(TokenStatics.getAlgorithm());
    }

    @Override
    public String genAccessToken(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + TokenStatics.ACCESS_TOKEN_TIME))
                .withIssuer(url)
                .withClaim("roles", roles)
                .sign(TokenStatics.getAlgorithm());
    }

    @Override
    public DecodedJWT extractAndDecodeJWTForTest(String token) {
        if (    token == null || token.length() <= TokenStatics.BEARER.length()
                || !token.startsWith(TokenStatics.BEARER))
            throw new JWTDecodeException("Token has not the correct format");

        return JWT.require(TokenStatics.getAlgorithm())
                .build()
                .verify(token.substring(TokenStatics.BEARER.length()));
    }
}
