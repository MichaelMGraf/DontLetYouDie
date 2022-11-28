package de.dontletyoudie.backend.persistence.account.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AccountAlreadyExistsException extends UsernameNotFoundException {
    public AccountAlreadyExistsException(String property) {
        super(property + " already exists");
    }
}
