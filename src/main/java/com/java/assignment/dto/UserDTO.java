package com.java.assignment.dto;

import com.java.assignment.annotation.PastDate;
import com.java.assignment.util.constant.ConstantsList;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

import static com.java.assignment.util.constant.ConstantsList.EMAIL_REGEX;

public record UserDTO(
        @Email(regexp = EMAIL_REGEX,
                message = "{email.message}")
        @NotNull(message = "{notNull.email}")
        String email,
        @NotNull(message = "{notNull.message}")
        String firstName,
        @NotNull(message = "{notNull.lastName}")
        String lastName,
        @PastDate(message = "{pastDate.message}")
        @NotNull(message = "{notNull.message}")
        Date birthDate,
        String address,
        String phoneNumber) {
}