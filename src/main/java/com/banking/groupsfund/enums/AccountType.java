package com.banking.groupsfund.enums;

public enum AccountType {
    GROUP_FUND("Quỹ nhóm"),
    SAVINGS("Quỹ tiết kiệm"),
    CURRENT("Quỹ phát sinh");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
