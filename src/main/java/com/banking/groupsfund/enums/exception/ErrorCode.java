package com.banking.groupsfund.enums.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ── Generic ──────────────────────────────────────────────────────────────
    INTERNAL_ERROR(500, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ARGUMENT(400, "Tham số không hợp lệ", HttpStatus.BAD_REQUEST),

    // ── Auth ─────────────────────────────────────────────────────────────────
    EMAIL_ALREADY_EXISTS(1001, "Email đã được sử dụng", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS(1002, "Số điện thoại đã được sử dụng", HttpStatus.CONFLICT),
    INVALID_OTP(1003, "Mã OTP không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST),
    REGISTRATION_SESSION_EXPIRED(1004, "Phiên đăng ký đã hết hạn, vui lòng thực hiện lại", HttpStatus.GONE),
    ACCOUNT_DISABLED(1005, "Tài khoản đã bị khoá", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(1006, "Email/số điện thoại hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),

    // ── Customer ─────────────────────────────────────────────────────────────
    CUSTOMER_NOT_FOUND(2001, "Không tìm thấy khách hàng", HttpStatus.NOT_FOUND),

    // ── Account (Quỹ) ────────────────────────────────────────────────────────
    ACCOUNT_NOT_FOUND(3001, "Không tìm thấy quỹ", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(3002, "Quỹ không ở trạng thái hoạt động", HttpStatus.UNPROCESSABLE_ENTITY),

    // ── Invitation (Mã mời) ──────────────────────────────────────────────────
    INVITATION_NOT_FOUND(4001, "Mã mời không tồn tại", HttpStatus.NOT_FOUND),
    INVITATION_EXPIRED(4002, "Mã mời đã hết hạn", HttpStatus.GONE),
    INVITATION_USAGE_EXHAUSTED(4003, "Mã mời đã hết lượt sử dụng", HttpStatus.CONFLICT),
    ALREADY_MEMBER(4004, "Bạn đã là thành viên của quỹ này", HttpStatus.CONFLICT),

    // ── Approval (Yêu cầu chi) ───────────────────────────────────────────────
    APPROVAL_REQUEST_NOT_FOUND(5001, "Không tìm thấy yêu cầu chi", HttpStatus.NOT_FOUND),
    APPROVAL_ALREADY_SIGNED(5002, "Bạn đã ký cho yêu cầu này rồi", HttpStatus.CONFLICT),
    APPROVAL_NOT_PENDING(5003, "Yêu cầu không ở trạng thái chờ duyệt", HttpStatus.UNPROCESSABLE_ENTITY),
    APPROVAL_NOT_APPROVED(5004, "Yêu cầu chưa được phê duyệt để ghi sổ", HttpStatus.UNPROCESSABLE_ENTITY),
    APPROVAL_AMOUNT_INVALID(5005, "Số tiền yêu cầu phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    APPROVAL_MIN_SIGNATURE_INVALID(5006, "Cần ít nhất 1 chữ ký để tạo yêu cầu", HttpStatus.BAD_REQUEST),
    ACCOUNT_HAS_PENDING_APPROVALS(5007, "Quỹ còn yêu cầu chi đang chờ duyệt, không thể đóng quỹ", HttpStatus.CONFLICT),
    ACCOUNT_BALANCE_NOT_SETTLED(5008, "Quỹ còn số dư chưa được phân bổ hết", HttpStatus.CONFLICT),

    // ── Transaction (Giao dịch) ──────────────────────────────────────────────
    TRANSACTION_INVALID_STATE(6001, "Giao dịch không thể chuyển sang trạng thái này", HttpStatus.UNPROCESSABLE_ENTITY),

    // ── Ledger (Bút toán) ────────────────────────────────────────────────────
    LEDGER_AMOUNT_INVALID(7001, "Số tiền bút toán phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
