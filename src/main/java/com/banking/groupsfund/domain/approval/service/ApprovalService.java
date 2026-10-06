package com.banking.groupsfund.domain.approval.service;

import java.util.UUID;

import com.banking.groupsfund.domain.approval.entity.ApprovalRequest;
import com.banking.groupsfund.domain.approval.enums.Decision;

public interface ApprovalService {
	ApprovalRequest sign(UUID requestId, UUID customerId, Decision decision);
}
