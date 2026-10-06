package com.banking.groupsfund.domain.approval.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.approval.entity.ApprovalRequest;
import com.banking.groupsfund.domain.approval.enums.ApprovalStatus;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, UUID> {
    boolean existsByAccountIdAndStatus(UUID accountId, ApprovalStatus status);
}
