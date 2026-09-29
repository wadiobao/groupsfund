package com.banking.groupsfund.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.banking.groupsfund.dto.ApiResponse;
import com.banking.groupsfund.exception.custom.BussinessException;
import com.banking.groupsfund.exception.custom.NotFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {
            NotFoundException.class,
    })
    public <T> ResponseEntity<ApiResponse<T>> handleException(NotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(ApiResponse.<T>error(exception.getMessage(), status));
    }

    @ExceptionHandler(value = {
            BussinessException.class,
    })
    public <T> ResponseEntity<ApiResponse<T>> handleException(BussinessException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(ApiResponse.<T>error(exception.getMessage(), status));
    }
}
