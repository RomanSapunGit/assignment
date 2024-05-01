package com.java.assignment.dto;

import java.sql.Timestamp;

public record ResponseExceptionDTO(String causedBy, Timestamp timestamp, String message) {
}
