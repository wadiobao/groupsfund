package com.banking.groupsfund.enums;

public enum MemberRole {
    MEMBER("Thành viên"),
    TREASURER("Thủ quỹ");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
