package de.dontletyoudie.backend.persistence.account.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    String password;

    @JsonProperty("username")
    private String username;
}
