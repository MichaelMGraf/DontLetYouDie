package de.dontletyoudie.backend.persistence.relationship.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class FriendReturnDTO {

    @JsonProperty
    List<String> usernames;

}
