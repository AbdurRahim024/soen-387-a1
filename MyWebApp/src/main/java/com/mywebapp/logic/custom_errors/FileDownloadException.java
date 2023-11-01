package com.mywebapp.logic.custom_errors;

public class FileDownloadException extends Exception {
    public FileDownloadException(String s) {
        super(s);
    }
}
