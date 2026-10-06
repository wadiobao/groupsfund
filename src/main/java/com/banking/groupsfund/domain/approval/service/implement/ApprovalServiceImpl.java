package com.banking.groupsfund.domain.approval.service.implement;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.groupsfund.domain.approval.entity.ApprovalRequest;
import com.banking.groupsfund.domain.approval.entity.ApprovalSignature;
import com.banking.groupsfund.domain.approval.enums.Decision;
import com.banking.groupsfund.domain.approval.repository.ApprovalRequestRepository;
import com.banking.groupsfund.domain.approval.repository.ApprovalSignatureRepository;
import com.banking.groupsfund.domain.approval.service.ApprovalService;
import com.banking.groupsfund.enums.exception.ErrorCode;
import com.banking.groupsfund.exception.custom.ConflictException;
import com.banking.groupsfund.exception.custom.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ApprovalServiceImpl implements ApprovalService{

	private final ApprovalRequestRepository requestRepository;
    private final ApprovalSignatureRepository signatureRepository;

    @Override
    @Transactional
    public ApprovalRequest sign(UUID requestId, UUID customerId, Decision decision) {
        ApprovalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.APPROVAL_REQUEST_NOT_FOUND));

        if (signatureRepository.existsByApprovalRequestIdAndCustomerId(requestId, customerId)) {
            throw new ConflictException(ErrorCode.APPROVAL_ALREADY_SIGNED);
        }

        signatureRepository.save(new ApprovalSignature(requestId, customerId, decision));

        if (decision == Decision.APPROVE) {
            request.registerApproveSignature();   // tự chuyển APPROVED nếu đủ ngưỡng
        } else {
            request.reject();
        }
        requestRepository.save(request);
        return request;
    }
}
