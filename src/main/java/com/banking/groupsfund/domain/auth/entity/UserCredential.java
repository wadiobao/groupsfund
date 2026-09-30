package com.banking.groupsfund.domain.auth.entity;

import com.banking.groupsfund.domain.customer.entity.Customer;
import com.banking.groupsfund.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * Lưu thông tin xác thực của khách hàng (credentials).
 * Tách biệt hoàn toàn với {@link Customer} (hồ sơ nghiệp vụ).
 */
@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCredential extends BaseEntity {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    Customer customer;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "phone_number", unique = true)
    String phoneNumber;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    boolean isActive = true;
}
