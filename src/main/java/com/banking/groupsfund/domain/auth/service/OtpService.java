package com.banking.groupsfund.domain.auth.service;

public interface OtpService {

    String generateAndStore(String email);

    boolean verify(String email, String otp);
}
