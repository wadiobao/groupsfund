package com.banking.groupsfund.domain.approval.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.approval.entity.ApprovalSignature;

@Repository
public interface ApprovalSignatureRepository extends JpaRepository<ApprovalSignature, UUID> {
    boolean existsByApprovalRequestIdAndCustomerId(UUID approvalRequestId, UUID customerId);
}