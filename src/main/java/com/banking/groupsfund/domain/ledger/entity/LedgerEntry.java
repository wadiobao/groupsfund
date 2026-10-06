package com.banking.groupsfund.domain.ledger.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.banking.groupsfund.domain.ledger.enums.Direction;
import com.banking.groupsfund.domain.ledger.enums.EntryType;
import com.banking.groupsfund.entity.ImmutableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ledger_entries")
@NoArgsConstructor
public class LedgerEntry extends ImmutableEntity {

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 6)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private EntryType entryType = EntryType.PRINCIPAL;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    public LedgerEntry(UUID transactionId, UUID accountId, Direction direction, EntryType entryType,
            BigDecimal amount) {
        if (amount.signum() <= 0) {
			throw new IllegalArgumentException("Số tiền bút toán phải dương");
		}
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.direction = direction;
        this.entryType = entryType;
        this.amount = amount;
    }

}
