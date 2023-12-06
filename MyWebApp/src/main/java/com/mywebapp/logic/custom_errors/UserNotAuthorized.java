package com.mywebapp.logic.custom_errors;

public class UserNotAuthorized extends Exception {
    public UserNotAuthorized(String s) {
        super(s);
    }
}
