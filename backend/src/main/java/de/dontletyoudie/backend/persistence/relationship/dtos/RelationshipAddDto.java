package de.dontletyoudie.backend.persistence.relationship.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class RelationshipAddDto {
    @JsonProperty
    private String srcUsername;

    @JsonProperty
    private String relUsername;
}
