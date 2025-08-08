CREATE TABLE daily_report (
                               member_id BIGINT NOT NULL,
                               daily_invest_total DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                               daily_invest_count INT NOT NULL DEFAULT 0 ,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
                               is_deleted TINYINT(1) NOT NULL DEFAULT 0,
)

CREATE TABLE weekly_report (
                              member_id BIGINT NOT NULL,
                              weekly_total_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                              weekly_total_count INT NOT NULL DEFAULT 0 ,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
                              is_deleted TINYINT(1) NOT NULL DEFAULT 0,
)

CREATE TABLE monthly_report (
                               member_id BIGINT NOT NULL,
                               monthly_total_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                               monthly_total_count INT NOT NULL DEFAULT 0 ,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
                               is_deleted TINYINT(1) NOT NULL DEFAULT 0,
)