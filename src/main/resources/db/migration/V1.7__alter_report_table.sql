ALTER TABLE daily_report
    ADD COLUMN daily_valuation_total DECIMAL(15,2) DEFAULT 0.00 COMMENT '일일 평가금액 총합';

ALTER TABLE weekly_report
    ADD COLUMN weekly_valuation_total DECIMAL(15,2) DEFAULT 0.00 COMMENT '주간 평가금액 총합';

ALTER TABLE monthly_report
    ADD COLUMN monthly_valuation_total DECIMAL(15,2) DEFAULT 0.00 COMMENT '월간 평가금액 총합';