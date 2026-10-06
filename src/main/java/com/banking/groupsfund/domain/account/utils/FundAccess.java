package com.banking.groupsfund.domain.account.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.banking.groupsfund.domain.account.entity.AccountMember;
import com.banking.groupsfund.domain.account.repository.AccountMemberRepository;
import com.banking.groupsfund.enums.MemberRole;

import lombok.RequiredArgsConstructor;

@Component("fundAccess")
@RequiredArgsConstructor
public class FundAccess {

    private final AccountMemberRepository memberRepository;

    public boolean isMember(UUID accountId, UUID customerId) {
        return memberRepository.existsById(new AccountMember.AccountMemberId(accountId, customerId));
    }

    public boolean isTreasurer(UUID accountId, UUID customerId) {
        return memberRepository.findById(new AccountMember.AccountMemberId(accountId, customerId))
                .map(m -> m.getRole() == MemberRole.TREASURER)
                .orElse(false);
    }
}
