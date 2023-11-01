package com.mywebapp.logic.custom_errors;

public class CustomerOrderMismatchException extends Exception {
    public CustomerOrderMismatchException(String s) {
        super(s);
    }
}
