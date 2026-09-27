package com.banking.groupsfund.domain.account.dto;

import java.math.BigDecimal;

import com.banking.groupsfund.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    @NotBlank
    @Size(min = 1, max = 150)
    private String name; // "Quỹ lớp 22CS1"

    @NotNull
    private AccountType accountType;

    @Positive
    @Min(0)
    private BigDecimal goalAmount; // nullable — không phải quỹ nào cũng có mục tiêu

    @NotNull
    private Integer openingBalance;
}
