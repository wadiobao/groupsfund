package com.banking.groupsfund.domain.account.service.implement;

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
import com.banking.groupsfund.enums.MemberRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final AccountMemberRepository memberRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request, UUID creatorId) {

        Account account = accountFactory.create(request.getAccountType(), request.getName(), request.getGoalAmount(),
                creatorId);
        accountRepository.save(account);

        var membership = new AccountMember(account.getId(), creatorId, MemberRole.TREASURER);
        memberRepository.save(membership);

        return AccountResponse.from(account);
    }

}
