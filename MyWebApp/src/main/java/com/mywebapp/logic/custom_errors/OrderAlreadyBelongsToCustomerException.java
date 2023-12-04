package com.mywebapp.logic.custom_errors;

public class OrderAlreadyBelongsToCustomerException extends Exception {
    public OrderAlreadyBelongsToCustomerException(String s) {
        super(s);
    }
}
