package com.banking.groupsfund.domain.account.service.implement;

import org.springframework.stereotype.Service;

import com.banking.groupsfund.domain.account.dto.AccountResponse;
import com.banking.groupsfund.domain.account.dto.CreateAccountRequest;
import com.banking.groupsfund.domain.account.repository.AccountRepository;
import com.banking.groupsfund.domain.account.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        return null;
    }

}
