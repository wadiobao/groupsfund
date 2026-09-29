package com.banking.groupsfund.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.banking.groupsfund.domain.customer.entity.Customer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	private final SecretKey key;

    public JwtService(@Value("${app.security.jjwt.secret-key}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Customer customer) {
        return Jwts.builder()
            .subject(customer.getId().toString())
            .claim("role", customer.getRole()) 
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 giờ
            .signWith(key)
            .compact();
    }

    public Claims verifyToken(String token) {
        return Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).getPayload();
    }
}

