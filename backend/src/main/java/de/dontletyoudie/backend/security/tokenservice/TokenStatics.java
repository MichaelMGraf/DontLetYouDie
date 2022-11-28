package de.dontletyoudie.backend.security.tokenservice;

import com.auth0.jwt.algorithms.Algorithm;

final class TokenStatics {

    final static String BEARER = "Bearer ";
    final static String secret = "DDSecretSignature";

    final static long ACCESS_TOKEN_TIME = 7 * 24 * 3600 * 1000;
    final static long REFRESH_TOKEN_TIME = 365L * 24 * 3600 * 1000;
    private static Algorithm algorithm;

    private TokenStatics(){}

    static Algorithm getAlgorithm() {
        if (algorithm == null) algorithm = Algorithm.HMAC256(secret.getBytes());
        return algorithm;
    }
}
