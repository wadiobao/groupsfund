package com.banking.groupsfund.enums;

public enum CustomerRole {
    USER("user"),
    ADMIN("admin");

    private final String value;

    CustomerRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
