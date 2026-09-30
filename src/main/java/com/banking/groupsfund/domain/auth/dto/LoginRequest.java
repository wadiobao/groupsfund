package com.banking.groupsfund.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;


public record LoginRequest(

        @NotBlank(message = "Username (email or phone) is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {}
