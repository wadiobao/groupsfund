package com.banking.groupsfund.domain.account.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.groupsfund.domain.account.entity.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);
}
