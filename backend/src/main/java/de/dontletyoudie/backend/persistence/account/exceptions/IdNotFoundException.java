package de.dontletyoudie.backend.persistence.account.exceptions;

public class IdNotFoundException extends Exception {
    public IdNotFoundException(String id) {
        super(id + " already exists");
    }

    public IdNotFoundException(long id) {
        this(id + "");
    }
}
