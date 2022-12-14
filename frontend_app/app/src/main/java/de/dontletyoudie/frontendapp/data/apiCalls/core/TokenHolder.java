package de.dontletyoudie.frontendapp.data.apiCalls.core;

class TokenHolder {
    private static String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXNzaTAzMDUiLCJyb2xlcyI6WyJVU0VSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9sb2dpbiIsImV4cCI6MTY3MDM2MjY5Nn0.V4FuCHqw578w2uFLIykQj0d8A8VlCAUGIkeUopGpDWQ";
    private static String REFRESH_TOKEN  ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXNzaTAzMDUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvbG9naW4iLCJleHAiOjE2NzA2MDg4MzR9.bNS0SoYiP0OuTKF0Sm_qbfRDLDYF5rP7fGzBKiWEb9Y";
    static String getAccessToken() {
        if (ACCESS_TOKEN == null) throw new IllegalStateException("no Token saved");
        return ACCESS_TOKEN;
    }

    static String getRefreshToken() {
        if (REFRESH_TOKEN == null) throw new IllegalStateException("no Token saved");
        return REFRESH_TOKEN;
    }

    static void setAccessToken(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }

    static void setRefreshToken(String refreshToken) {
        REFRESH_TOKEN = refreshToken;
    }
}
