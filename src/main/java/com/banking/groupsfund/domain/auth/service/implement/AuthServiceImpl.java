package com.banking.groupsfund.domain.auth.service.implement;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.groupsfund.domain.auth.dto.AuthResponse;
import com.banking.groupsfund.domain.auth.dto.LoginRequest;
import com.banking.groupsfund.domain.auth.dto.RegisterRequest;
import com.banking.groupsfund.domain.auth.dto.VerifyOtpRequest;
import com.banking.groupsfund.domain.auth.entity.UserCredential;
import com.banking.groupsfund.domain.auth.repository.UserCredentialRepository;
import com.banking.groupsfund.domain.auth.service.AuthService;
import com.banking.groupsfund.domain.auth.service.OtpService;
import com.banking.groupsfund.domain.customer.entity.Customer;
import com.banking.groupsfund.domain.customer.repository.CustomerRepository;
import com.banking.groupsfund.exception.custom.BussinessException;
import com.banking.groupsfund.exception.custom.NotFoundException;
import com.banking.groupsfund.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Key prefix lưu thông tin đăng ký tạm trong Redis (trước khi OTP xác thực)
    private static final String PENDING_PREFIX = "register:pending:";

    private final CustomerRepository customerRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.otp.expiry-seconds:300}")
    private long expirySeconds;

    /**
     * Bước 1: kiểm tra trùng lặp → lưu thông tin đăng ký tạm vào Redis → sinh & lưu OTP.
     * Dữ liệu tạm được lưu dưới dạng pipe-separated string, TTL bằng với TTL của OTP.
     *
     * @return OTP để dev/test; production sẽ gửi qua email/SMS thay vì trả về đây.
     */
    @Override
    public String initRegister(RegisterRequest request) {
        if (userCredentialRepository.existsByEmail(request.email())) {
            throw new BussinessException("Email already in use: " + request.email());
        }
        if (userCredentialRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BussinessException("Phone number already in use: " + request.phoneNumber());
        }

        // Lưu thông tin đăng ký tạm thời vào Redis (TTL = thời gian sống OTP)
        // Format: fullName|phoneNumber|gender|passwordHash
        String pendingKey = PENDING_PREFIX + request.email().toLowerCase();
        String hashedPassword = passwordEncoder.encode(request.password());
        String pendingValue = String.join("|",
                request.fullName(),
                request.phoneNumber(),
                request.gender() != null ? request.gender() : "",
                hashedPassword);
        redisTemplate.opsForValue().set(pendingKey, pendingValue, Duration.ofSeconds(expirySeconds));

        // Sinh OTP và lưu vào Redis
        return otpService.generateAndStore(request.email());
    }

    /**
     * Bước 2: xác thực OTP → lấy dữ liệu tạm từ Redis → tạo Customer + UserCredential.
     */
    @Override
    @Transactional
    public AuthResponse completeRegister(VerifyOtpRequest request) {
        // Xác thực OTP — sẽ tự xoá OTP sau khi verify thành công
        boolean valid = otpService.verify(request.email(), request.otp());
        if (!valid) {
            throw new BussinessException("OTP is invalid or has expired");
        }

        // Lấy thông tin đăng ký tạm từ Redis
        String pendingKey = PENDING_PREFIX + request.email().toLowerCase();
        String pendingValue = redisTemplate.opsForValue().get(pendingKey);
        if (pendingValue == null) {
            throw new NotFoundException("Registration session has expired. Please start over.");
        }

        // Parse dữ liệu tạm (fullName|phoneNumber|gender|passwordHash)
        String[] parts = pendingValue.split("\\|", -1);
        String fullName    = parts[0];
        String phoneNumber = parts[1];
        String gender      = parts[2].isBlank() ? null : parts[2];
        String passwordHash = parts[3];

        // Tạo hồ sơ nghiệp vụ
        Customer customer = Customer.builder()
                .fullName(fullName)
                .email(request.email())
                .phoneNumber(phoneNumber)
                .gender(gender)
                .build();
        customer = customerRepository.save(customer);

        // Tạo credential với password đã hash sẵn từ bước 1
        UserCredential credential = UserCredential.builder()
                .customer(customer)
                .email(request.email())
                .phoneNumber(phoneNumber)
                .passwordHash(passwordHash)
                .build();
        userCredentialRepository.save(credential);

        // Dọn dẹp dữ liệu tạm trong Redis
        redisTemplate.delete(pendingKey);

        String token = jwtService.generateToken(customer);
        return toAuthResponse(token, customer, request.email());
    }

    /**
     * Đăng nhập: nhận email hoặc SĐT, xác thực mật khẩu, trả về JWT.
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        UserCredential credential = request.username().contains("@")
                ? userCredentialRepository.findByEmail(request.username())
                        .orElseThrow(() -> new BussinessException("Invalid email or password"))
                : userCredentialRepository.findByPhoneNumber(request.username())
                        .orElseThrow(() -> new BussinessException("Invalid phone number or password"));

        if (!credential.isActive()) {
            throw new BussinessException("Account is disabled. Please contact support.");
        }

        if (!passwordEncoder.matches(request.password(), credential.getPasswordHash())) {
            throw new BussinessException("Invalid email or password");
        }

        Customer customer = credential.getCustomer();
        String token = jwtService.generateToken(customer);
        return toAuthResponse(token, customer, credential.getEmail());
    }

    private AuthResponse toAuthResponse(String token, Customer customer, String email) {
        return new AuthResponse(
                token,
                customer.getId(),
                customer.getFullName(),
                email,
                customer.getRole());
    }
}
