package com.banking.groupsfund.enums;

public enum KycStatus {
    UNVERIFIED("Chưa xác minh"),
    PENDING("Chờ xác minh"),
    VERIFIED("Đã xác minh"),
    REJECTED("Đã từ chối");

    private String value;

    KycStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
