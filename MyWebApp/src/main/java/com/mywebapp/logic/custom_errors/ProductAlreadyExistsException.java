package com.mywebapp.logic.custom_errors;

public class ProductAlreadyExistsException extends Exception {
    public ProductAlreadyExistsException(String s) {
        super(s);
    }
}
