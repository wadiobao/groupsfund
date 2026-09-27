package com.banking.groupsfund.domain.account.entity;

import java.time.Instant;
import java.util.UUID;

import com.banking.groupsfund.entity.BaseEntity;
import com.banking.groupsfund.enums.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "invitations", indexes = {
        @Index(name = "idx_invitation_code", columnList = "invite_code", unique = true),

})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invitation extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    UUID accountId;

    @Column(name = "invite_code", nullable = false, unique = true, length = 20)
    String inviteCode;

    @Column(name = "created_by", nullable = false)
    UUID createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_to_assign", nullable = false)
    MemberRole roleToAssign;

    @Column(name = "max_uses", nullable = false)
    int maxUses;

    @Column(name = "used_count", nullable = false)
    int usedCount;

    @Column(name = "expires_at", nullable = false)
    Instant expiresAt;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isExhausted() {
        return usedCount >= maxUses;
    }

    public boolean isUsable() {
        return !isExpired() && !isExhausted();
    }

    /**
     * Tăng lượt dùng — ném lỗi ngay nếu vi phạm quy tắc, không để Service tự kiểm
     * tra rồi quên.
     */
    public void redeem() {
        if (isExpired())
            throw new IllegalStateException("Mã mời đã hết hạn");
        if (isExhausted())
            throw new IllegalStateException("Mã mời đã hết lượt sử dụng");
        this.usedCount++;
    }
}
