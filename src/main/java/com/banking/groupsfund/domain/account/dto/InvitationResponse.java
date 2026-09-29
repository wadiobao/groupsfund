package com.banking.groupsfund.domain.account.dto;

import java.time.Instant;

import com.banking.groupsfund.domain.account.entity.Invitation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitationResponse {
    private String inviteCode;
    private String inviteLink;
    private Instant expiresAt;

    public static InvitationResponse from(Invitation inv) {
        return InvitationResponse.builder()
                .inviteCode(inv.getInviteCode())
                .inviteLink("/join/" + inv.getInviteCode())
                .expiresAt(inv.getExpiresAt())
                .build();
    }
}
