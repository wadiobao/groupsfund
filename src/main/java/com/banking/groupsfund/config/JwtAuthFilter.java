package com.banking.groupsfund.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.banking.groupsfund.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }
        try {
            Claims claims = jwtService.verifyToken(authHeader.substring(7));
            // Dùng CustomerPrincipal để lưu trữ thông tin người dùng
            // var principal = new CustomerPrincipal(UUID.fromString(claims.getSubject()));

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
            var auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JwtException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        chain.doFilter(req, res);
    }
}
