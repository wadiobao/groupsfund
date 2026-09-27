package com.banking.groupsfund.domain.account.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.groupsfund.domain.account.dto.AccountResponse;
import com.banking.groupsfund.domain.account.dto.CreateAccountRequest;
import com.banking.groupsfund.domain.account.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal String creatorId) {

        UUID creatorUuid = UUID.fromString(creatorId);

        AccountResponse response = accountService.createAccount(request, creatorUuid);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
