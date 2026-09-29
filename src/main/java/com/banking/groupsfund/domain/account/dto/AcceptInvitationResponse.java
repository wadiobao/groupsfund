package com.banking.groupsfund.domain.account.dto;

import java.util.UUID;

import com.banking.groupsfund.enums.MemberRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcceptInvitationResponse {
    private UUID accountId;
    private String accountName;
    private MemberRole yourRole;
}
