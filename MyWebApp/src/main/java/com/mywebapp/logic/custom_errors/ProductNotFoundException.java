package com.mywebapp.logic.custom_errors;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String s) {
        super(s);
    }
}
