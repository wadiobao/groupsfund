package com.banking.groupsfund.domain.approval.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.banking.groupsfund.domain.approval.enums.ApprovalStatus;
import com.banking.groupsfund.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "approval_requests")
public class ApprovalRequest extends BaseEntity {  

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "transaction_id")
    private UUID transactionId;   // null cho đến khi POSTED

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(length = 255)
    private String description;

    @Column(name = "requested_by", nullable = false)
    private UUID requestedBy;

    @Column(name = "required_signatures", nullable = false)
    private int requiredSignatures;

    @Column(name = "current_signature_count", nullable = false)
    private int currentSignatureCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    protected ApprovalRequest() {}

    public ApprovalRequest(UUID accountId, BigDecimal amount, String description,
                            UUID requestedBy, int requiredSignatures) {
        if (amount.signum() <= 0) {
			throw new IllegalArgumentException("Số tiền phải dương");
		}
        if (requiredSignatures < 1) {
			throw new IllegalArgumentException("Cần ít nhất 1 chữ ký");
		}
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
        this.requestedBy = requestedBy;
        this.requiredSignatures = requiredSignatures;
    }

    /**
     * Ghi nhận 1 chữ ký duyệt — tự chuyển sang APPROVED nếu đã đủ số lượng.
     * Logic ngưỡng nằm NGAY TRONG entity, không phó mặc cho Service tự đếm rồi quên kiểm tra.
     */
    public void registerApproveSignature() {
        if (status != ApprovalStatus.PENDING) {
            throw new IllegalStateException("Chỉ ký được khi yêu cầu đang ở trạng thái PENDING");
        }
        this.currentSignatureCount++;
        if (this.currentSignatureCount >= this.requiredSignatures) {
            this.status = ApprovalStatus.APPROVED;
        }
    }

    public void reject() {
        if (status != ApprovalStatus.PENDING) {
            throw new IllegalStateException("Chỉ từ chối được khi yêu cầu đang ở trạng thái PENDING");
        }
        this.status = ApprovalStatus.REJECTED;
    }

    /** Gọi sau khi đã ghi Transaction/LedgerEntry thành công cho yêu cầu này */
    public void markPosted(UUID transactionId) {
        if (status != ApprovalStatus.APPROVED) {
            throw new IllegalStateException("Chỉ ghi sổ được khi yêu cầu đã APPROVED");
        }
        this.transactionId = transactionId;
        this.status = ApprovalStatus.POSTED;
    }
}