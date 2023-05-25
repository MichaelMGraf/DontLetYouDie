package de.dontletyoudie.frontendapp.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class ProofGetDto {
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


    public String getUsername() {
        return username;
    }

    public byte[] getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

    public boolean isApproved() {
        return approved;
    }

    public long getProofId() {
        return proofId;
    }
}
