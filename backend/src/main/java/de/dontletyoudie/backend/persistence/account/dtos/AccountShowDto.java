package de.dontletyoudie.backend.persistence.account.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dontletyoudie.backend.persistence.account.Account;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@ToString
public class AccountShowDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    public AccountShowDto() {
        username = "";
        email = "";
    }

    public AccountShowDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public AccountShowDto(Account account) {
        this.username = account.getUsername();
        this.email = account.getEmail();
    }
}
