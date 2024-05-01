package com.java.assignment.annotation.implementation;

import com.java.assignment.annotation.PastDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PastDateValidator implements ConstraintValidator<PastDate, Date> {
    @Override
    public void initialize(PastDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }

        var currentDate = LocalDate.now();
        var birthLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return birthLocalDate.isBefore(currentDate);
    }
}
