package com.madeeasy.exception;

public class InvalidAccountException extends ATMException {
    public InvalidAccountException(String message) {
        super(message);
    }
}