package com.madeeasy.exception;

public class UnauthorizedAccessException extends ATMException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
