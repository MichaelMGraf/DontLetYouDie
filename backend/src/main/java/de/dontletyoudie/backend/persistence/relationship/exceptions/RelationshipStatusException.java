package de.dontletyoudie.backend.persistence.relationship.exceptions;

import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;

public class RelationshipStatusException extends Exception {
    public RelationshipStatusException(String srcAccount, String relAccount, RelationshipStatus is, RelationshipStatus should) {
        super("Relationship between " + srcAccount + " and " + relAccount + " is " + is + " but should be " + should);
    }
}
