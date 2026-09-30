package com.banking.groupsfund.domain.auth.service.implement;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.banking.groupsfund.domain.auth.service.OtpService;

import lombok.RequiredArgsConstructor;

@Service
public class OtpServiceImpl implements OtpService {

    // Key prefix trong Redis để tránh xung đột với các key khác
    private static final String OTP_PREFIX = "otp:register:";

    private final StringRedisTemplate redisTemplate;

    private final long expirySeconds;

    private final int otpLength;

    private final SecureRandom secureRandom;

    public OtpServiceImpl(@Value("app.otp.expiry-seconds") long expirySeconds,
            @Value("app.otp.length") int otpLength, StringRedisTemplate redisTemplate) {
        this.expirySeconds = expirySeconds;
        this.otpLength = otpLength;
        this.redisTemplate = redisTemplate;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public String generateAndStore(String email) {
        String otp = generateOtp();
        String redisKey = OTP_PREFIX + email.toLowerCase();

        // Lưu vào Redis với TTL tự động xoá sau khi hết hạn
        redisTemplate.opsForValue().set(redisKey, otp, Duration.ofSeconds(expirySeconds));
        return otp;
    }

    @Override
    public boolean verify(String email, String otp) {
        String redisKey = OTP_PREFIX + email.toLowerCase();
        String storedOtp = redisTemplate.opsForValue().get(redisKey);

        if (storedOtp == null || !storedOtp.equals(otp)) {
            return false;
        }

        // Xoá OTP ngay sau khi dùng — one-time use
        redisTemplate.delete(redisKey);
        return true;
    }

    private String generateOtp() {
        // Tạo số nguyên ngẫu nhiên rồi format thành chuỗi đủ otpLength chữ số
        int bound = (int) Math.pow(10, otpLength);
        int number = secureRandom.nextInt(bound);
        return String.format("%0" + otpLength + "d", number);
    }
}
