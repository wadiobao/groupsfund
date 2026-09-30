package com.banking.groupsfund.domain.auth.service;

import com.banking.groupsfund.domain.auth.dto.AuthResponse;
import com.banking.groupsfund.domain.auth.dto.LoginRequest;
import com.banking.groupsfund.domain.auth.dto.RegisterRequest;
import com.banking.groupsfund.domain.auth.dto.VerifyOtpRequest;

public interface AuthService {

    String initRegister(RegisterRequest request);

    AuthResponse completeRegister(VerifyOtpRequest request);

    AuthResponse login(LoginRequest request);
}
