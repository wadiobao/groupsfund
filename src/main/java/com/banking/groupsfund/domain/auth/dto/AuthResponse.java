package com.banking.groupsfund.domain.auth.dto;

import java.util.UUID;

import com.banking.groupsfund.enums.CustomerRole;

public record AuthResponse(
        String accessToken,
        UUID customerId,
        String fullName,
        String email,
        CustomerRole role) {
}
