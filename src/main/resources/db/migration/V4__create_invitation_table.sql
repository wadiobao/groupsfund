-- Flyway migration V4
-- Bảng invitations — mã mời/link tham gia quỹ
-- Phụ thuộc: customers (V1), accounts (V2)

CREATE TABLE invitations (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      UUID NOT NULL REFERENCES accounts(id),
    invite_code     VARCHAR(20) NOT NULL UNIQUE,
    created_by      UUID NOT NULL REFERENCES customers(id),
    role_to_assign  VARCHAR(20) NOT NULL DEFAULT 'MEMBER'
                        CHECK (role_to_assign IN ('MEMBER','TREASURER')),
    max_uses        INT NOT NULL DEFAULT 1,
    used_count      INT NOT NULL DEFAULT 0,
    expires_at      TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_invitations_code ON invitations(invite_code);