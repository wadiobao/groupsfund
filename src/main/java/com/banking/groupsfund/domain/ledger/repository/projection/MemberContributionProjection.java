package com.banking.groupsfund.domain.ledger.repository.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface MemberContributionProjection {
    UUID getCustomerId();

    BigDecimal getTotalContributed();
}
