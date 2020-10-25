package com.adrian.mocanu.atm.exception;

public class ExceededAmountException extends RuntimeException {

    public ExceededAmountException(String message) {
        super(message);
    }
}
