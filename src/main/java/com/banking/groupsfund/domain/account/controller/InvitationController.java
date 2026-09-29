package com.banking.groupsfund.domain.account.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.groupsfund.domain.account.dto.AcceptInvitationResponse;
import com.banking.groupsfund.domain.account.dto.InvitationRequest;
import com.banking.groupsfund.domain.account.dto.InvitationResponse;
import com.banking.groupsfund.domain.account.service.InvitationService;
import com.banking.groupsfund.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/invitations")
@Validated
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/{accountId}/invitations")
    public ResponseEntity<ApiResponse<InvitationResponse>> createInvitation(
            @PathVariable UUID accountId,
            @Valid @RequestBody InvitationRequest req,
            @AuthenticationPrincipal String creatorId) {

        UUID creatorUuid = UUID.fromString(creatorId);

        InvitationResponse invitation = invitationService.create(req, accountId, creatorUuid);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Invitation created successfully", invitation));
    }

    @PostMapping("/{inviteCode}/accept")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ApiResponse<AcceptInvitationResponse>> accept(
            @PathVariable String inviteCode,
            @AuthenticationPrincipal String customerId) {

        UUID customerUuid = UUID.fromString(customerId);

        var result = invitationService.accept(inviteCode, customerUuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Invitation accepted successfully", result));
    }
}
