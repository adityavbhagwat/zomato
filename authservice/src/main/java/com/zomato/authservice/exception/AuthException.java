package com.zomato.authservice.exception;

public class AuthException extends RuntimeException {
    private int code;

    public AuthException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
