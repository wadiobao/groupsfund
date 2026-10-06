package com.banking.groupsfund.domain.ledger.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.ledger.entity.LedgerEntry;
import com.banking.groupsfund.domain.ledger.enums.Direction;
import com.banking.groupsfund.domain.ledger.enums.EntryType;
import com.banking.groupsfund.domain.ledger.repository.projection.MemberContributionProjection;
import com.banking.groupsfund.domain.transaction.enums.TransactionStatus;
import com.banking.groupsfund.domain.transaction.enums.TransactionType;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

	@Query("""
		    SELECT t.initiatedBy AS customerId,
		           SUM(le.amount) AS totalContributed
		    FROM LedgerEntry le, Transaction t
		    WHERE le.transactionId = t.id
		      AND le.accountId = :accountId
		      AND le.direction = :direction
		      AND le.entryType = :entryType
		      AND t.type = :transactionType
		      AND t.status = :status
		    GROUP BY t.initiatedBy
		    """)
		List<MemberContributionProjection> sumContributionsByMember(
		    @Param("accountId") UUID accountId,
		    @Param("direction") Direction direction,
		    @Param("entryType") EntryType entryType,
		    @Param("transactionType") TransactionType transactionType,
		    @Param("status") TransactionStatus status
		);
}
