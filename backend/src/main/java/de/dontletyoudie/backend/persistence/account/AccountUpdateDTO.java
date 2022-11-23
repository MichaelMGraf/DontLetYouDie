package de.dontletyoudie.backend.persistence.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccountUpdateDTO extends AccountAddDTO {

    @JsonProperty("id")
    private long id;

    public AccountUpdateDTO(String name,  String password, String email, long id) {
        super(name, email, password);
        this.id = id;
    }
}
