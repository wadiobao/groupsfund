package com.banking.groupsfund.domain.account.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.account.entity.AccountMember;

@Repository
public interface AccountMemberRepository extends JpaRepository<AccountMember, UUID> {

}
