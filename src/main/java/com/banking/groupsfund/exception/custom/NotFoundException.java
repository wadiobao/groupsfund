package com.banking.groupsfund.exception.custom;

import com.banking.groupsfund.enums.exception.ErrorCode;

public class NotFoundException extends AppException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
