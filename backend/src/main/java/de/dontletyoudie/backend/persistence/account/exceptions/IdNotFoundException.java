package de.dontletyoudie.backend.persistence.account.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class IdNotFoundException extends UsernameNotFoundException {
    public IdNotFoundException(String id) {
        super(id + " already exists");
    }

    public IdNotFoundException(long id) {
        this(id + "");
    }
}
