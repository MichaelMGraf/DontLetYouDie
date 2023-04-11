package de.dontletyoudie.backend.persistence.account.exceptions;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(String username) {
        super("Username: " + username + " not found");
    }

    public AccountNotFoundException(int id) {
        super("ID: " + id + " not found");
    }
}
