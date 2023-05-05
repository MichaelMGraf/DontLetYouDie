package de.dontletyoudie.backend.persistence.proof.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.awt.*;
import java.time.ZonedDateTime;

@Getter
@ToString
@AllArgsConstructor
public class ProofReturnDto {

    @JsonProperty("username")
    String username;

    byte[] image;

    @JsonProperty("category")
    String category;

    @JsonProperty("comment")
    String comment;

    @JsonProperty("avgScore")
    float avgScore;

    @JsonProperty("judgements")
    int judgements;

    @JsonProperty("proofId")
    long proofId = 1;

}
