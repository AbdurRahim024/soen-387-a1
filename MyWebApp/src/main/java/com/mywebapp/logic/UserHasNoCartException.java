package com.mywebapp.logic;

class UserHasNoCartException extends Exception {
    public UserHasNoCartException(String s) {
        super(s);
    }
}
