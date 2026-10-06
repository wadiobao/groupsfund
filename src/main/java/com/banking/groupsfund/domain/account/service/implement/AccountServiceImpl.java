package com.banking.groupsfund.domain.account.service.implement;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.groupsfund.domain.account.common.AccountFactory;
import com.banking.groupsfund.domain.account.dto.AccountResponse;
import com.banking.groupsfund.domain.account.dto.CreateAccountRequest;
import com.banking.groupsfund.domain.account.entity.Account;
import com.banking.groupsfund.domain.account.entity.AccountMember;
import com.banking.groupsfund.domain.account.repository.AccountMemberRepository;
import com.banking.groupsfund.domain.account.repository.AccountRepository;
import com.banking.groupsfund.domain.account.service.AccountService;
import com.banking.groupsfund.domain.approval.enums.ApprovalStatus;
import com.banking.groupsfund.domain.approval.repository.ApprovalRequestRepository;
import com.banking.groupsfund.enums.MemberRole;
import com.banking.groupsfund.enums.exception.ErrorCode;
import com.banking.groupsfund.exception.custom.ConflictException;
import com.banking.groupsfund.exception.custom.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final AccountMemberRepository memberRepository;
    private final ApprovalRequestRepository approvalRequestRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request, UUID creatorId) {

        Account account = accountFactory.create(request.getAccountType(), request.getName(), request.getGoalAmount(),
                creatorId);
        accountRepository.save(account);

        var membership = new AccountMember(account.getId(), creatorId, MemberRole.TREASURER);
        memberRepository.save(membership);

        return AccountResponse.from(account);
    }

    @Override 
    public AccountResponse closeAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        // Ràng buộc 1: không còn khoản chờ duyệt
        boolean hasPendingApprovals = approvalRequestRepository
                .existsByAccountIdAndStatus(accountId, ApprovalStatus.PENDING);
        if (hasPendingApprovals) {
            throw new ConflictException(ErrorCode.ACCOUNT_HAS_PENDING_APPROVALS);
        }

        // Ràng buộc 2: đã rút gọn hết nợ — số dư phải về 0
        BigDecimal ledgerBalance = accountRepository.findLedgerBalance(accountId);
        if (ledgerBalance.compareTo(BigDecimal.ZERO) != 0) {
            throw new ConflictException(ErrorCode.ACCOUNT_BALANCE_NOT_SETTLED);
        }

        account.close();
        accountRepository.save(account);
        return AccountResponse.from(account);
    }

}
