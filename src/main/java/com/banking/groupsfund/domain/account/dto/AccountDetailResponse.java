package com.banking.groupsfund.domain.account.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.banking.groupsfund.enums.AccountStatus;
import com.banking.groupsfund.enums.MemberRole;

public record AccountDetailResponse(
        UUID id,
        String name,
        AccountStatus status,
        BigDecimal goalAmount,
        BigDecimal ledgerBalance,   
        BigDecimal goalProgressPercent, // null nếu quỹ không đặt mục tiêu
        List<MemberDetailResponse> members) {

}
