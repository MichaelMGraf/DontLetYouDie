package de.dontletyoudie.backend.persistence.relationship.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RelationshipAddDto {
    @JsonProperty
    private String srcUsername;

    @JsonProperty
    private String relUsername;
}
