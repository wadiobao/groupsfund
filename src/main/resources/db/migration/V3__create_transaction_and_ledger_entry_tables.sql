-- Flyway migration V3
-- Bảng transactions (log sự kiện) và ledger_entries (bút toán Debit/Credit, bất biến)
-- Phụ thuộc: customers (V1), accounts (V2)

CREATE TABLE transactions (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type                VARCHAR(20) NOT NULL
                            CHECK (type IN ('DEPOSIT','WITHDRAWAL','TRANSFER','FEE','INTEREST')),
    status              VARCHAR(20) NOT NULL DEFAULT 'INITIATED'
                            CHECK (status IN ('INITIATED','PENDING','POSTED','FAILED','REVERSED')),
    idempotency_key     VARCHAR(100) NOT NULL UNIQUE,
    description         VARCHAR(255),
    initiated_by        UUID NOT NULL REFERENCES customers(id),
    reversal_of_id      UUID REFERENCES transactions(id),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    posted_at           TIMESTAMPTZ
);

CREATE TABLE ledger_entries (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id      UUID NOT NULL REFERENCES transactions(id),
    account_id          UUID NOT NULL REFERENCES accounts(id),
    direction           VARCHAR(6) NOT NULL CHECK (direction IN ('DEBIT','CREDIT')),
    entry_type          VARCHAR(20) NOT NULL DEFAULT 'PRINCIPAL'
                            CHECK (entry_type IN ('PRINCIPAL','FEE','INTEREST')),
    amount              NUMERIC(18,2) NOT NULL CHECK (amount > 0),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
    -- Không có updated_at — bảng chỉ INSERT, không UPDATE/DELETE ở tầng ứng dụng
);

CREATE INDEX idx_ledger_entries_account_id ON ledger_entries(account_id);
CREATE INDEX idx_ledger_entries_account_created ON ledger_entries(account_id, created_at);
CREATE INDEX idx_ledger_entries_transaction_id ON ledger_entries(transaction_id);
CREATE INDEX idx_transactions_initiated_by ON transactions(initiated_by);

-- View tính số dư động — KHÔNG lưu cột balance ở bảng accounts (đúng nguyên lý double-entry)
CREATE VIEW account_balance AS
SELECT
    a.id AS account_id,
    a.name,
    COALESCE(SUM(CASE WHEN le.direction = 'CREDIT' THEN le.amount ELSE 0 END), 0)
    - COALESCE(SUM(CASE WHEN le.direction = 'DEBIT' THEN le.amount ELSE 0 END), 0) AS ledger_balance
FROM accounts a
LEFT JOIN ledger_entries le ON le.account_id = a.id
GROUP BY a.id, a.name;