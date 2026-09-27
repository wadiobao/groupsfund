package com.banking.groupsfund.domain.account.service;

import com.banking.groupsfund.domain.account.dto.AccountResponse;
import com.banking.groupsfund.domain.account.dto.CreateAccountRequest;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request);

    
}
