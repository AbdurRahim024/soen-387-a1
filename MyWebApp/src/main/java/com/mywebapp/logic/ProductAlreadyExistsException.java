package com.mywebapp.logic;

public class ProductAlreadyExistsException extends Exception {
    public ProductAlreadyExistsException(String s) {
        super(s);
    }
}
