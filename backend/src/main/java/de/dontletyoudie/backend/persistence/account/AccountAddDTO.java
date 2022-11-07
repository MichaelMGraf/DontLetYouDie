package de.dontletyoudie.backend.persistence.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AccountAddDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    public AccountAddDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
