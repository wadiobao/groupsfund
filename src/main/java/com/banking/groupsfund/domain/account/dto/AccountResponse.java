package com.banking.groupsfund.domain.account.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.banking.groupsfund.domain.account.entity.Account;
import com.banking.groupsfund.enums.AccountStatus;
import com.banking.groupsfund.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private UUID id;
    private String name;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal goalAmount;
    private Instant lockedUntil;
    private UUID createdBy;
    private Instant closedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public static AccountResponse from(Account account) {
        if (account == null) {
            return null;
        }
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .goalAmount(account.getGoalAmount())
                .lockedUntil(account.getLockedUntil())
                .createdBy(account.getCreatedBy())
                .closedAt(account.getClosedAt())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
