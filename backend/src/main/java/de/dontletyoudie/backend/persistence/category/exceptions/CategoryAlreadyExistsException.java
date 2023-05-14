package de.dontletyoudie.backend.persistence.category.exceptions;

public class CategoryAlreadyExistsException extends Exception {
    public CategoryAlreadyExistsException(String category) {
        super(category + " already exists");
    }
}