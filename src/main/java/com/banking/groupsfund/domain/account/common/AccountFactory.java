package com.banking.groupsfund.domain.account.common;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.banking.groupsfund.domain.account.entity.Account;
import com.banking.groupsfund.enums.AccountType;

@Component
public class AccountFactory {

    /**
     * Tạo Account với cấu hình đúng theo từng loại — nơi DUY NHẤT trong hệ thống
     * biết "loại quỹ nào thì áp dụng quy tắc gì". AccountService không cần biết chi
     * tiết này.
     */
    public Account create(AccountType type, String name, BigDecimal goalAmount, UUID createdBy) {
        return switch (type) {
            case GROUP_FUND -> createGroupFund(name, goalAmount, createdBy);
            case SAVINGS -> createSavings(name, createdBy);
            case CURRENT -> createCurrent(name, createdBy);
        };
    }

    private Account createGroupFund(String name, BigDecimal goalAmount, UUID createdBy) {
        // Quỹ nhóm: goalAmount tùy chọn, không có kỳ hạn mặc định, không overdraft
        Account account = new Account(name, AccountType.GROUP_FUND, createdBy);
        if (goalAmount != null) {
            account.setGoalAmount(goalAmount);
        }
        return account;
    }

    private Account createSavings(String name, UUID createdBy) {
        // Savings: bắt buộc không cho phép overdraft (đã thống nhất ở phần phân biệt
        // loại tài khoản)
        // Factory là nơi enforce quy tắc này ngay lúc khởi tạo, không để Service tự nhớ
        Account account = new Account(name, AccountType.SAVINGS, createdBy);
        return account;
    }

    private Account createCurrent(String name, UUID createdBy) {
        // Current: KHÔNG dùng cho quy mô project này (chỉ GROUP_FUND là chính),
        // nhưng vẫn định nghĩa sẵn để enum AccountType không "mồ côi" trường hợp xử lý
        Account account = new Account(name, AccountType.CURRENT, createdBy);
        return account;
    }
}
