package com.banking.groupsfund.domain.account.service.implement;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.groupsfund.domain.account.dto.AcceptInvitationResponse;
import com.banking.groupsfund.domain.account.dto.InvitationRequest;
import com.banking.groupsfund.domain.account.dto.InvitationResponse;
import com.banking.groupsfund.domain.account.entity.Account;
import com.banking.groupsfund.domain.account.entity.AccountMember;
import com.banking.groupsfund.domain.account.entity.AccountMember.AccountMemberId;
import com.banking.groupsfund.domain.account.entity.Invitation;
import com.banking.groupsfund.domain.account.repository.AccountMemberRepository;
import com.banking.groupsfund.domain.account.repository.AccountRepository;
import com.banking.groupsfund.domain.account.repository.InvitationRepository;
import com.banking.groupsfund.domain.account.service.InvitationService;
import com.banking.groupsfund.enums.MemberRole;
import com.banking.groupsfund.enums.exception.ErrorCode;
import com.banking.groupsfund.exception.custom.BussinessException;
import com.banking.groupsfund.exception.custom.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final AccountRepository accountRepository;
    private final AccountMemberRepository memberRepository;
    private final SecureRandom random;

    @Override
    public InvitationResponse create(InvitationRequest request, UUID accountId, UUID creatorId) {
        String code = generateUniqueCode();
        Invitation invitation = Invitation.builder()
                .accountId(accountId)
                .createdBy(creatorId)
                .inviteCode(code)
                .roleToAssign(MemberRole.valueOf(request.getRoleToAssign()))
                .maxUses(request.getMaxUses())
                .expiresAt(Instant.now().plus(request.getExpiresInHours(), ChronoUnit.HOURS))
                .build();
        invitationRepository.save(invitation);
        return InvitationResponse.from(invitation);
    }

    @Override
    public AcceptInvitationResponse accept(String inviteCode, UUID customerId) {
        Invitation invitation = invitationRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException(ErrorCode.INVITATION_NOT_FOUND));

        invitation.redeem(); // tự kiểm tra hết hạn/hết lượt và tăng usedCount — ném lỗi nếu vi phạm

        if (memberRepository.existsById(new AccountMember.AccountMemberId(invitation.getAccountId(), customerId))) {
            throw new BussinessException(ErrorCode.ALREADY_MEMBER);
        }

        var membership = new AccountMember(invitation.getAccountId(), customerId, invitation.getRoleToAssign());
        memberRepository.save(membership);
        invitationRepository.save(invitation);

        Account account = accountRepository.findById(invitation.getAccountId()).orElseThrow();
        return AcceptInvitationResponse.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .yourRole(invitation.getRoleToAssign())
                .build();
    }

    private String generateUniqueCode() {
        // Mã ngắn, dễ đọc, tránh ký tự dễ nhầm (0/O, 1/I)
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++)
            sb.append(chars.charAt(random.nextInt(chars.length())));
        String code = sb.toString();
        return invitationRepository.existsByInviteCode(code) ? generateUniqueCode() : code; // đảm bảo không trùng
    }
}
