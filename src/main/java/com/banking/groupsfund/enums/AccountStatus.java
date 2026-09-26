package com.banking.groupsfund.enums;

public enum AccountStatus {

    ACTIVE("Đang hoạt động"),
    LOCKED("Đã khóa"),
    CLOSED("Đã đóng");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
