-- Flyway migration V2
-- Bảng accounts (quỹ) và account_members (quan hệ nhiều-nhiều + role)
-- Phụ thuộc: customers (V1)

CREATE TABLE accounts (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                VARCHAR(150) NOT NULL,
    account_type        VARCHAR(20)  NOT NULL DEFAULT 'GROUP_FUND'
                            CHECK (account_type IN ('GROUP_FUND','SAVINGS','CURRENT')),
    status              VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE'
                            CHECK (status IN ('ACTIVE','LOCKED','CLOSED')),
    goal_amount         NUMERIC(18,2),
    locked_until        TIMESTAMPTZ,
    created_by          UUID NOT NULL REFERENCES customers(id),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    closed_at           TIMESTAMPTZ
);

CREATE TABLE account_members (
    account_id      UUID NOT NULL REFERENCES accounts(id),
    customer_id     UUID NOT NULL REFERENCES customers(id),
    role            VARCHAR(20) NOT NULL DEFAULT 'MEMBER'
                        CHECK (role IN ('MEMBER','TREASURER')),
    joined_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (account_id, customer_id)
);

CREATE INDEX idx_account_members_customer_id ON account_members(customer_id);