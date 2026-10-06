package com.banking.groupsfund.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
    private String message;
    private Instant timestamp;

    public ErrorResponse(String error, String message, Instant timestamp) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

}
