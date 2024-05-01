package com.java.assignment.exception;

public class DateRangeNotValidException extends Exception {
    private static final String DEFAULT_MESSAGE = "Date range is not valid!";

    public DateRangeNotValidException() {
        super(DEFAULT_MESSAGE);
    }
}
