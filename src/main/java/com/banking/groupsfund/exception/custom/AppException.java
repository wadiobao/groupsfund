package com.banking.groupsfund.exception.custom;

import com.banking.groupsfund.enums.exception.ErrorCode;

import lombok.Getter;

@Getter
public abstract class AppException extends RuntimeException {

    private ErrorCode errorCode;

    public AppException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
