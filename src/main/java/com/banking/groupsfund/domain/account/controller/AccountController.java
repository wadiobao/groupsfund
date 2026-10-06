package com.banking.groupsfund.domain.account.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.groupsfund.domain.account.dto.AccountDetailResponse;
import com.banking.groupsfund.domain.account.dto.AccountResponse;
import com.banking.groupsfund.domain.account.dto.CreateAccountRequest;
import com.banking.groupsfund.domain.account.entity.Account;
import com.banking.groupsfund.domain.account.service.AccountQueryService;
import com.banking.groupsfund.domain.account.service.AccountService;
import com.banking.groupsfund.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountQueryService accountQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal String creatorId) {

        UUID creatorUuid = UUID.fromString(creatorId);

        AccountResponse response = accountService.createAccount(request, creatorUuid);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully", response));
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("@fundAccess.isMember(#accountId, principal.customerId())") // chỉ thành viên trong quỹ mới xem được
    ResponseEntity<AccountDetailResponse> getDetail(@PathVariable UUID accountId) {
        return ResponseEntity.ok(accountQueryService.getAccountDetail(accountId));
    }

    @PostMapping("/{accountId}/close")
    @PreAuthorize("@fundAccess.isTreasurer(#accountId, principal.customerId())")
    public ResponseEntity<ApiResponse<AccountResponse>> close(@PathVariable UUID accountId) {
        AccountResponse response = accountService.closeAccount(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Account closed successfully", response));
    }

}
