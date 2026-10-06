package com.banking.groupsfund.domain.ledger.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MemberContribution(
        UUID customerId,
        BigDecimal totalContributed) {
}
