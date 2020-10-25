package com.adrian.mocanu.atm.exception;

public class OutOfFundsException extends RuntimeException {

    public OutOfFundsException(String message) {
        super(message);
    }
}
