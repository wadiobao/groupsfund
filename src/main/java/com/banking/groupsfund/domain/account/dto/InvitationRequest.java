package com.banking.groupsfund.domain.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationRequest {
    @NotNull
    private String roleToAssign;
    @NotNull
    private Integer maxUses;
    @NotNull
    private Integer expiresInHours;
}
