package de.dontletyoudie.frontendapp.data.apiCalls.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TokenEntity {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("refresh_token")
    private String refresh_token;

    @Override
    public String toString() {
        return "Entity{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }

    public static TokenEntity getTokenFromResponse(@NotNull String responseBody) throws IOException {
        return new ObjectMapper().readValue(responseBody,
                TokenEntity.class);
    }

    public void saveTokens() {
        TokenHolder.setAccessToken(access_token);
        TokenHolder.setRefreshToken(refresh_token);
    }

    public static void deleteTokens() {
        TokenHolder.setAccessToken(null);
        TokenHolder.setRefreshToken(null);
    }
}
