package com.banking.groupsfund.domain.account.service.implement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banking.groupsfund.domain.account.dto.AccountDetailResponse;
import com.banking.groupsfund.domain.account.dto.MemberDetailResponse;
import com.banking.groupsfund.domain.account.entity.Account;
import com.banking.groupsfund.domain.account.entity.AccountMember;
import com.banking.groupsfund.domain.account.repository.AccountMemberRepository;
import com.banking.groupsfund.domain.account.repository.AccountRepository;
import com.banking.groupsfund.domain.account.service.AccountQueryService;
import com.banking.groupsfund.domain.customer.entity.Customer;
import com.banking.groupsfund.domain.customer.repository.CustomerRepository;
import com.banking.groupsfund.domain.ledger.enums.Direction;
import com.banking.groupsfund.domain.ledger.enums.EntryType;
import com.banking.groupsfund.domain.ledger.repository.LedgerEntryRepository;
import com.banking.groupsfund.domain.ledger.repository.projection.MemberContributionProjection;
import com.banking.groupsfund.domain.transaction.enums.TransactionStatus;
import com.banking.groupsfund.domain.transaction.enums.TransactionType;
import com.banking.groupsfund.enums.exception.ErrorCode;
import com.banking.groupsfund.exception.custom.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountRepository accountRepository;
    private final AccountMemberRepository memberRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final CustomerRepository customerRepository;

    @Override
	public AccountDetailResponse getAccountDetail(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        BigDecimal ledgerBalance = accountRepository.findLedgerBalance(accountId);

        BigDecimal goalProgress = account.getGoalAmount() != null && account.getGoalAmount().signum() > 0
                ? ledgerBalance.divide(account.getGoalAmount(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                : null; // quỹ không đặt mục tiêu thì không có % này

        List<AccountMember> members = memberRepository.findByAccountId(accountId);
        Map<UUID, BigDecimal> contributionMap = ledgerEntryRepository.sumContributionsByMember(
        		accountId,
        	    Direction.CREDIT,
        	    EntryType.PRINCIPAL,
        	    TransactionType.DEPOSIT,
        	    TransactionStatus.POSTED).stream()
                .collect(Collectors.toMap(MemberContributionProjection::getCustomerId,
                        MemberContributionProjection::getTotalContributed));

        BigDecimal totalContributed = contributionMap.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MemberDetailResponse> memberResponses = members.stream().map(m -> {
            BigDecimal contributed = contributionMap.getOrDefault(m.getAccountMemberId().getCustomerId(), BigDecimal.ZERO);
            BigDecimal percent = totalContributed.signum() > 0
                    ? contributed.divide(totalContributed, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO;
            Customer customer = customerRepository.findById(m.getAccountMemberId().getCustomerId()).orElseThrow();
            return new MemberDetailResponse(customer.getId(), customer.getFullName(), m.getRole(), contributed,
                    percent);
        }).toList();

        return new AccountDetailResponse(account.getId(), account.getName(), account.getStatus(),
                account.getGoalAmount(), ledgerBalance, goalProgress, memberResponses);
    }

}
