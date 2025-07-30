CREATE TABLE goal (
                      object_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '목표 고유 ID',
                      member_id BIGINT NOT NULL COMMENT '사용자 ID (member 테이블의 member_id 참조)',
                      object_name VARCHAR(255) NOT NULL COMMENT '목표명',
                      target_amount BIGINT NOT NULL COMMENT '목표 금액',
                        start_date DATE NOT NULL COMMENT '목표 시작일',
                        end_date DATE NOT NULL COMMENT '목표 종료일',
                        deposit_rate INT NOT NULL COMMENT '예적금 할당 비율',
                      isa_rate INT NOT NULL COMMENT 'ISA 할당 비율',
                        start_amount BIGINT NOT NULL  COMMENT '초기금액',
                        goal_rate INT NOT NULL comment '목표달성률',
                        PRIMARY KEY (`object_id`),
                        CONSTRAINT `fk_object_user_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자 목표 설정 정보';

