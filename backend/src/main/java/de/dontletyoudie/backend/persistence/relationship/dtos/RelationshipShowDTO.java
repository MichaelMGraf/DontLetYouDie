package de.dontletyoudie.backend.persistence.relationship.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RelationshipShowDTO {
    @JsonProperty
    String sourceAccount;
    @JsonProperty
    String relatedAccount;
    @JsonProperty
    RelationshipStatus relationshipStatus;
}
