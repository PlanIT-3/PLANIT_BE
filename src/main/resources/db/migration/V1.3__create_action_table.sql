CREATE TABLE action (
                        action_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '액션 ID',
                        object_id BIGINT NOT NULL COMMENT '객체 ID',
                        account_number VARCHAR(50) COMMENT '계좌번호',
                        member_product_id BIGINT COMMENT '회원 상품 ID',
                        allocated_rate INT COMMENT '예적금 / ISA 할당 비율',
                        account_allocated_rate INT COMMENT '예적금 계좌별 할당 비율',
                        account_type VARCHAR(20) COMMENT '계좌 유형',
                        amount INT COMMENT '금액',
                        action_type VARCHAR(20) COMMENT '액션 유형 (ISA, DEPOSIT)',
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

                        PRIMARY KEY (action_id),
                        INDEX idx_object_id (object_id),
                        INDEX idx_member_product_id (member_product_id),
                        INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;