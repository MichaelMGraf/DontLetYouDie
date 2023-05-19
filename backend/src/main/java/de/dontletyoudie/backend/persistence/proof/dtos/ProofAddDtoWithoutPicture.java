package de.dontletyoudie.backend.persistence.proof.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class ProofAddDtoWithoutPicture {
    @JsonProperty("username")
    String username;

    @JsonProperty("creationDate")
    LocalDateTime creationDate;

    @JsonProperty("category")
    String category;

    @JsonProperty("comment")
    String comment;
}
