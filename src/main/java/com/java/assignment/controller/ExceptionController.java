package com.java.assignment.controller;

import com.java.assignment.dto.ResponseExceptionDTO;
import com.java.assignment.exception.DateRangeNotValidException;
import com.java.assignment.exception.InappropriateAgeException;
import com.java.assignment.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ResponseExceptionDTO> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseExceptionDTO(e.getClass().getName(),
                        Timestamp.from(ZonedDateTime.now().toInstant()),
                        e.getMessage()));
    }

    @ExceptionHandler(value = {InappropriateAgeException.class, DateRangeNotValidException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    protected ResponseEntity<ResponseExceptionDTO> handleRequestException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseExceptionDTO(e.getClass().getName(),
                        Timestamp.from(ZonedDateTime.now().toInstant()),
                        e.getMessage()));
    }

    @ExceptionHandler(value={UserNotFoundException.class, EntityNotFoundException.class})
    protected ResponseEntity<ResponseExceptionDTO> handleNotFoundResourceException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseExceptionDTO(e.getClass().getName(),
                        Timestamp.from(ZonedDateTime.now().toInstant()),
                        e.getMessage()));
    }
}
