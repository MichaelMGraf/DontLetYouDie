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

    @JsonProperty("avgScore")
    float avgScore;

    @JsonProperty("judgements")
    int judgements;

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

    public float getAvgScore() {
        return avgScore;
    }

    public int getJudgements() {
        return judgements;
    }

    public long getProofId() {
        return proofId;
    }
}
