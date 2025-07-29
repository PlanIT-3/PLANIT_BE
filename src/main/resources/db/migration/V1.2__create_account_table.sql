CREATE TABLE account (
                         account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         member_id BIGINT NOT NULL,
                         account_name VARCHAR(255) NOT NULL,
                         account_number VARCHAR(50) NOT NULL UNIQUE,
                         account_currency VARCHAR(10) NOT NULL DEFAULT 'KRW',
                         account_balance DECIMAL(20,2) NOT NULL DEFAULT 0.00,
                         account_deposit DECIMAL(20,2) NOT NULL DEFAULT 0.00,
                         earnings_rate DECIMAL(5,4) DEFAULT 0.0000,
                         account_invested_cost DECIMAL(20,2) NOT NULL DEFAULT 0.00,
                         last_tran_date TIMESTAMP NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);