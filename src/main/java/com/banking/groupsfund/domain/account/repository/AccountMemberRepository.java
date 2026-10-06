package com.banking.groupsfund.domain.account.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.account.entity.AccountMember;

@Repository
public interface AccountMemberRepository extends JpaRepository<AccountMember, AccountMember.AccountMemberId> {

    @Query("SELECT m FROM AccountMember m WHERE m.id.accountId = :accountId")
    List<AccountMember> findByAccountId(@Param("accountId") UUID accountId);
}
