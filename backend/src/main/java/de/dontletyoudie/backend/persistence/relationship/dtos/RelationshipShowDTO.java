package de.dontletyoudie.backend.persistence.relationship.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import de.dontletyoudie.backend.persistence.relationship.Relationship;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class RelationshipShowDTO {

    @JsonProperty
    String sourceAccount;
    @JsonProperty
    String relatedAccount;
    @JsonProperty
    RelationshipStatus relationshipStatus;

    public RelationshipShowDTO(Relationship relationship) {
        this(
                relationship.getSrcAccount().getUsername(),
                relationship.getRelAccount().getUsername(),
                relationship.getRelationshipStatus()
        );
    }
}
