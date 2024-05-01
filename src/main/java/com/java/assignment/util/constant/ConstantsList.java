package com.java.assignment.util.constant;

import lombok.Getter;

@Getter
public class ConstantsList {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String EMAIL_EXCEPTION_MESSAGE = "Email should be valid";
    public static final String USER_NOT_FOUND_WITH_EMAIL = "User not found with email: ";
    public static final String USER_NOT_FOUND_FOR_DELETION = "User not found for deletion";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME ="LastName";
    public static final String EMAIL = "test@example.com";

    public static final String PHONE_NUMBER = "0687934412";
    public static final String ADDRESS ="1234 St";
    public static final String USERS_URL_TEMPLATE = "/users";
}
