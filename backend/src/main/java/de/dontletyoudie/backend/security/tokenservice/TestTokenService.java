package de.dontletyoudie.backend.security.tokenservice;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Arrays;
import java.util.List;

public interface TestTokenService extends TokenService {

    default TokenDto genAccessAndRefreshToken(String username) {
        return genAccessAndRefreshToken(username, List.of("USER"));
    }

    default TokenDto genAccessAndRefreshToken(String username, String... roles) {
        return genAccessAndRefreshToken(username, Arrays.stream(roles).toList());
    }

    TokenDto genAccessAndRefreshToken(String username, List<String> roles);

    String genRefreshToken(String username);

    default String genAccessToken(String username) {
        return genAccessToken(username, List.of("USER"));
    }

    default String genAccessToken(String username, String... roles) {
        return genAccessToken(username, Arrays.stream(roles).toList());
    }
    String genAccessToken(String username, List<String> roles);
    DecodedJWT extractAndDecodeJWTForTest(String token);
}
