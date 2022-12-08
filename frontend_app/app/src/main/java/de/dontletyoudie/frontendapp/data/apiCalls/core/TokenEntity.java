package de.dontletyoudie.frontendapp.data.apiCalls.core;

import com.fasterxml.jackson.annotation.JsonProperty;

class TokenEntity {
    @JsonProperty("access_token")
    String access_token;
    @JsonProperty("refresh_token")
    String refresh_token;

    @Override
    public String toString() {
        return "Entity{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }
}
