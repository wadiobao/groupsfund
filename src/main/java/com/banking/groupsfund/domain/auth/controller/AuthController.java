package com.banking.groupsfund.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.groupsfund.domain.auth.dto.AuthResponse;
import com.banking.groupsfund.domain.auth.dto.LoginRequest;
import com.banking.groupsfund.domain.auth.dto.RegisterRequest;
import com.banking.groupsfund.domain.auth.dto.VerifyOtpRequest;
import com.banking.groupsfund.domain.auth.service.AuthService;
import com.banking.groupsfund.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/init")
    public ResponseEntity<ApiResponse<String>> initRegister(
            @Valid @RequestBody RegisterRequest request) {

        String otp = authService.initRegister(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success("OTP sent. Please verify within 5 minutes.", otp));
    }

    @PostMapping("/register/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> completeRegister(
            @Valid @RequestBody VerifyOtpRequest request) {

        AuthResponse response = authService.completeRegister(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}
