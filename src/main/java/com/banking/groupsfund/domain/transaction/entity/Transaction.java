package com.banking.groupsfund.domain.transaction.entity;

import java.time.Instant;
import java.util.UUID;

import com.banking.groupsfund.domain.transaction.enums.TransactionStatus;
import com.banking.groupsfund.domain.transaction.enums.TransactionType;
import com.banking.groupsfund.entity.ImmutableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
public class Transaction extends ImmutableEntity { // id + createdAt — transaction có thêm posted_at riêng bên dưới

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.INITIATED;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @Column(length = 255)
    private String description;

    @Column(name = "initiated_by", nullable = false)
    private UUID initiatedBy; // UUID thô, KHÔNG map @ManyToOne tới Customer

    @Column(name = "reversal_of_id")
    private UUID reversalOfId;

    @Column(name = "posted_at")
    private Instant postedAt;

    public Transaction(TransactionType type, String idempotencyKey, String description, UUID initiatedBy) {
        this.type = type;
        this.idempotencyKey = idempotencyKey;
        this.description = description;
        this.initiatedBy = initiatedBy;
    }

    /**
     * Chuyển trạng thái — logic tự bảo vệ nằm trong entity, không phó mặc cho
     * Service
     */
    public void markPosted() {
        if (status != TransactionStatus.PENDING && status != TransactionStatus.INITIATED) {
            throw new IllegalStateException("Chỉ chuyển sang POSTED từ INITIATED/PENDING");
        }
        this.status = TransactionStatus.POSTED;
        this.postedAt = Instant.now();
    }

    public void markFailed() {
        this.status = TransactionStatus.FAILED;
    }
}
