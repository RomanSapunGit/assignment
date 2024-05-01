package com.java.assignment.dto;

import com.java.assignment.annotation.PastDate;
import com.java.assignment.util.constant.ConstantsList;
import jakarta.validation.constraints.Email;

import java.util.Date;

import static com.java.assignment.util.constant.ConstantsList.EMAIL_REGEX;

public record UserUpdateRequestDTO(
        String firstName,
        String lastName,
        @Email(regexp = EMAIL_REGEX, message = "{email.message}")
        String email,
        @PastDate()
        Date birthDate,
        String address,
        String phoneNumber) {
}
