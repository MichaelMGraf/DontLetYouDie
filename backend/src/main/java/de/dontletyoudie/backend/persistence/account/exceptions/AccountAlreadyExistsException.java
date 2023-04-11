package de.dontletyoudie.backend.persistence.account.exceptions;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String property) {
        super(property + " already exists");
    }
}
