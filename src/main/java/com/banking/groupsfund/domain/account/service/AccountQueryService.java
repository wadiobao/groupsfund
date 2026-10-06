package com.banking.groupsfund.domain.account.service;

import java.util.UUID;

import com.banking.groupsfund.domain.account.dto.AccountDetailResponse;

public interface AccountQueryService {
    public AccountDetailResponse getAccountDetail(UUID accountId);
}
