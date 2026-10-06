package com.banking.groupsfund.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.banking.groupsfund.dto.ApiResponse;
import com.banking.groupsfund.dto.ErrorResponse;
import com.banking.groupsfund.exception.custom.BussinessException;
import com.banking.groupsfund.exception.custom.ConflictException;
import com.banking.groupsfund.exception.custom.NotFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {
        @ExceptionHandler(value = {
                        NotFoundException.class,
        })
        public ResponseEntity<ErrorResponse> handleException(NotFoundException exception) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                return ResponseEntity
                                .status(status)
                                .body(new ErrorResponse(exception.getErrorCode().name(),
                                                exception.getErrorCode().getMessage(), Instant.now()));
        }

        @ExceptionHandler(value = {
                        BussinessException.class,
        })
        public ResponseEntity<ErrorResponse> handleException(BussinessException exception) {
                HttpStatus status = HttpStatus.BAD_REQUEST;
                return ResponseEntity
                                .status(status)
                                .body(new ErrorResponse(exception.getErrorCode().name(),
                                                exception.getErrorCode().getMessage(), Instant.now()));
        }

        @ExceptionHandler(value = {
                        ConflictException.class,
        })
        public ResponseEntity<ErrorResponse> handleException(ConflictException exception) {
                HttpStatus status = HttpStatus.CONFLICT;
                return ResponseEntity
                                .status(status)
                                .body(new ErrorResponse(exception.getErrorCode().name(),
                                                exception.getErrorCode().getMessage(), Instant.now()));
        }
}
