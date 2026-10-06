package com.banking.groupsfund.domain.approval.entity;

import java.util.UUID;

import com.banking.groupsfund.domain.approval.enums.Decision;
import com.banking.groupsfund.entity.ImmutableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "approval_signatures")
public class ApprovalSignature extends ImmutableEntity {   // chỉ ghi, không sửa — 1 chữ ký là sự kiện đã xảy ra

    @Column(name = "approval_request_id", nullable = false)
    private UUID approvalRequestId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Decision decision;

    protected ApprovalSignature() {}

    public ApprovalSignature(UUID approvalRequestId, UUID customerId, Decision decision) {
        this.approvalRequestId = approvalRequestId;
        this.customerId = customerId;
        this.decision = decision;
    }

}
