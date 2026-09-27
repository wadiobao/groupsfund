package com.banking.groupsfund.domain.account.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.account.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

}
