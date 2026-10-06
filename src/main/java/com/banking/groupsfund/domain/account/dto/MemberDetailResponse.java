package com.banking.groupsfund.domain.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.banking.groupsfund.enums.MemberRole;

public record MemberDetailResponse(
    UUID customerId,
    String fullName,
    MemberRole role,
    BigDecimal totalContributed,
    BigDecimal contributionPercent
) {}
