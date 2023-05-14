package de.dontletyoudie.backend.persistence.category.exceptions;

public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String categoryName) {
        super("Category: " + categoryName + " not found");
    }
}
