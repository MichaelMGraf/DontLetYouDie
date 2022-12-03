package de.dontletyoudie.backend.persistence.proof.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
@AllArgsConstructor
public class ProofAddDtoWithoutPicture {
    @JsonProperty("username")
    String username;

    @JsonProperty("creationDate")
    ZonedDateTime creationDate;

    @JsonProperty("category")
    String category;

    @JsonProperty("comment")
    String comment;
}
