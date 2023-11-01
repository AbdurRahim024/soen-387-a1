package com.mywebapp.logic.custom_errors;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String s) {
        super(s);
    }
}
