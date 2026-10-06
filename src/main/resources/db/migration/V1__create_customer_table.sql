-- Flyway migration V1
-- Bảng customers — thành viên trong hệ thống

CREATE TABLE customers (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name       VARCHAR(150) NOT NULL,
    phone_number    VARCHAR(20)  NOT NULL UNIQUE,
    email           VARCHAR(150) UNIQUE,
    gender			VARCHAR(5),
    kyc_status      VARCHAR(20)  NOT NULL DEFAULT 'UNVERIFIED'
                        CHECK (kyc_status IN ('UNVERIFIED','PENDING','VERIFIED','REJECTED')),
   	role			VARCHAR(6) NOT NULL DEFAULT 'USER'
   						CHECK (role IN ('USER','ADMIN')),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS user_credentials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),

    -- Quan hệ @OneToOne với Customer
    customer_id UUID NOT NULL UNIQUE,

    -- Các thuộc tính chính
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Khóa ngoại tham chiếu tới bảng customers
    CONSTRAINT fk_user_credentials_customer 
        FOREIGN KEY (customer_id) 
        REFERENCES customers(id) 
        ON DELETE CASCADE
);

-- Index bổ sung để tối ưu tốc độ truy vấn đăng nhập (Search by Email/Phone)
CREATE INDEX IF NOT EXISTS idx_user_credentials_email ON user_credentials(email);
CREATE INDEX IF NOT EXISTS idx_user_credentials_phone ON user_credentials(phone_number) WHERE phone_number IS NOT NULL;