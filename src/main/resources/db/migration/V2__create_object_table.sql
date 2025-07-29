CREATE TABLE `object` (
                          `goal_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '목표 고유 ID',
                          `user_id` BIGINT NOT NULL COMMENT '사용자 ID (member 테이블의 member_id 참조)',
                          `goal_name` VARCHAR(255) NOT NULL COMMENT '목표명',
                          `goal_amount` BIGINT NOT NULL COMMENT '목표 금액',
                          `start_date` DATE NOT NULL COMMENT '목표 시작일',
                          `end_date` DATE NOT NULL COMMENT '목표 종료일',
                          `deposit_ratio` INT NOT NULL COMMENT '예적금 할당 비율', -- 여기서 공백 제거
                          `isa_ratio` INT NOT NULL COMMENT 'ISA 할당 비율',     -- 여기서 공백 제거
                          PRIMARY KEY (`goal_id`),
                          CONSTRAINT `fk_object_user_id` FOREIGN KEY (`user_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자 목표 설정 정보';