package com.example.uploadfile.excepion;

public class WhiteListUserException extends RuntimeException {

    public WhiteListUserException(String message) {
        super(message);
    }
}
