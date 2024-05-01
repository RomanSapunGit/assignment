package com.java.assignment.exception;

import org.springframework.beans.factory.annotation.Value;

public class InappropriateAgeException extends Exception {
    private static final String DEFAULT_MESSAGE = "Your age must be more than configured!";

    public InappropriateAgeException(Integer age) {
        super(DEFAULT_MESSAGE + " (" + age + ")");
    }
}
