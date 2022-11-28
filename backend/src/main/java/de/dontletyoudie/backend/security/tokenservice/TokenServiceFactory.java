package de.dontletyoudie.backend.security.tokenservice;


public final class TokenServiceFactory {
    private static final DefaultTokenService defaultTokenService = new DefaultTokenService();

    private TokenServiceFactory() {}

    public static TokenService getTokeService() {
        return defaultTokenService;
    }
}
