package de.dontletyoudie.backend.persistence.proof.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dontletyoudie.backend.persistence.category.Category;
import de.dontletyoudie.backend.persistence.proof.Proof;
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
    String category;

    @JsonProperty("comment")
    String comment;

    @JsonProperty("approved")
    boolean approved;

    @JsonProperty("proofId")
    long proofId;

    public ProofReturnDto(Proof proof) {
        username = proof.getAccount().getUsername();
        image = proof.getImage();
        category = proof.getCategory().getName();
        comment = proof.getComment();
        approved = proof.isApproved();
        proofId = proof.getId();
    }
}
