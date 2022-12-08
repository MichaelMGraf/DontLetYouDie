package de.dontletyoudie.frontendapp.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountAddDto {
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    public AccountAddDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}