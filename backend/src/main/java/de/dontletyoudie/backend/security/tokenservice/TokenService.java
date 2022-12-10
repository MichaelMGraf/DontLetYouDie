package de.dontletyoudie.backend.security.tokenservice;

import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface TokenService {

    TokenDto genAccessAndRefreshToken(String username, HttpServletRequest request, List<String> roles);
    String genRefreshToken(String username, HttpServletRequest request);
    String genAccessToken(String username, HttpServletRequest request, List<String> roles);
    Optional<DecodedJWT> extractAndDecodeJWT(String token);
}
