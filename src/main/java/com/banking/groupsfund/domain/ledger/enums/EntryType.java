package com.banking.groupsfund.domain.ledger.enums;

public enum EntryType {
    PRINCIPAL("Gốc"),
    FEE("Phí"),
    INTEREST("Lãi");

    private String name;

    EntryType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
