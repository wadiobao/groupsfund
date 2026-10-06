package com.banking.groupsfund.domain.account.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.account.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query(value = "SELECT ledger_balance FROM account_balance WHERE account_id = :accountId", nativeQuery = true)
    BigDecimal findLedgerBalance(@Param("accountId") UUID accountId);
}
