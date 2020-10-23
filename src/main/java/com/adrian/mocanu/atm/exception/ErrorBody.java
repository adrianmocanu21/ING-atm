package com.adrian.mocanu.atm.exception;

public class ErrorBody {

    private final int status;

    private final String title;

    private final String errorMessage;

    ErrorBody(int status, String title, String errorMessage) {
        this.status = status;
        this.title = title;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
