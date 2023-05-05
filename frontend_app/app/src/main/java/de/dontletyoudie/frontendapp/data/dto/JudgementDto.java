package de.dontletyoudie.frontendapp.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JudgementDto {
    @JsonProperty
    private String judgeName;

    @JsonProperty
    private Long proofId;

    @JsonProperty
    private Boolean approved;
}
