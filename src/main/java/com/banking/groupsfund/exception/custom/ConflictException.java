package com.banking.groupsfund.exception.custom;

import com.banking.groupsfund.enums.exception.ErrorCode;

public class ConflictException extends AppException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
