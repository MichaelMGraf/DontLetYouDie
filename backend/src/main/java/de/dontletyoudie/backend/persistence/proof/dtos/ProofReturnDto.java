package de.dontletyoudie.backend.persistence.proof.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dontletyoudie.backend.persistence.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class ProofReturnDto {

    @JsonProperty("username")
    String username;

    byte[] image;

    @JsonProperty("category")
    Category category;

    @JsonProperty("comment")
    String comment;

    @JsonProperty("approved")
    boolean approved;

    @JsonProperty("proofId")
    long proofId;

}
