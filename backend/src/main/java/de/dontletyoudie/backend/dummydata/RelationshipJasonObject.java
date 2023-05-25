package de.dontletyoudie.backend.dummydata;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class RelationshipJasonObject {

    public RelationshipJasonObject(long a1, long a2, byte status) {
        this.a1 = Math.min(a1, a2);
        this.a2 = Math.max(a1, a2);
        this.status = RelationshipStatus.values()[status];
    }

    @JsonProperty
    public long a1;

    @JsonProperty
    public long a2;

    @JsonProperty
    public RelationshipStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationshipJasonObject that = (RelationshipJasonObject) o;
        return a1 == that.a1 && a2 == that.a2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a1, a2);
    }

}
