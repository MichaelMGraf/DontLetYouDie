package de.dontletyoudie.backend.persistence.relationship.exceptions;


public class RelationshipNotFoundException extends Exception {

    public RelationshipNotFoundException(String srcAccount, String relAccount) {
        super("Relationship between " + srcAccount + " and " + relAccount + " not found");
    }
}
