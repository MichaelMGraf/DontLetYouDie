package de.dontletyoudie.frontendapp.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RelationshipAddDto {
    @JsonProperty
    private String srcUsername;

    @JsonProperty
    private String relUsername;

    public RelationshipAddDto(String srcUsername, String relUsername) {
        this.srcUsername = srcUsername;
        this.relUsername = relUsername;
    }
}
