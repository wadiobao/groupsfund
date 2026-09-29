package com.banking.groupsfund.domain.account.service;

import java.util.UUID;

import com.banking.groupsfund.domain.account.dto.AcceptInvitationResponse;
import com.banking.groupsfund.domain.account.dto.InvitationRequest;
import com.banking.groupsfund.domain.account.dto.InvitationResponse;

public interface InvitationService {

    InvitationResponse create(InvitationRequest request, UUID accountId, UUID creatorId);

    AcceptInvitationResponse accept(String inviteCode, UUID customerId);
}
