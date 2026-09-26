package com.banking.groupsfund.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.banking.groupsfund.enums.AccountStatus;
import com.banking.groupsfund.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name; // "Quỹ lớp 22CS1"

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    @Builder.Default
    private AccountType accountType = AccountType.GROUP_FUND;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "goal_amount", precision = 18, scale = 2)
    private BigDecimal goalAmount; // nullable — không phải quỹ nào cũng có mục tiêu

    @Column(name = "locked_until")
    private Instant lockedUntil; // nullable — quỹ có kỳ hạn

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "closed_at")
    private Instant closedAt;

    public void close() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Chỉ đóng được quỹ đang ACTIVE");
        }
        this.status = AccountStatus.CLOSED;
        this.closedAt = Instant.now();
    }
}
