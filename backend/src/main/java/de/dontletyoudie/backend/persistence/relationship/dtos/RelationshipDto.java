package de.dontletyoudie.backend.persistence.relationship.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dontletyoudie.backend.persistence.relationship.Relationship;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class RelationshipDto {

    @JsonProperty
    private String srcUsername;

    @JsonProperty
    private String relUsername;

    @JsonProperty
    private RelationshipStatus status;

    public RelationshipDto(String srcUsername, String relUsername, RelationshipStatus status) {
        this.srcUsername = srcUsername;
        this.relUsername = relUsername;
        this.status = status;
    }

    public RelationshipDto(Relationship relationship) {
        this(
                relationship.getSrcAccount().getUsername(),
                relationship.getRelAccount().getUsername(),
                relationship.getRelationshipStatus()
        );
    }
}
