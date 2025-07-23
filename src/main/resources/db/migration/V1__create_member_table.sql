CREATE TABLE `member` (
                          `member_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `role` VARCHAR(20) NOT NULL COMMENT '사용자 역할',
                          `connected_id` VARCHAR(20) DEFAULT NULL,
                          `reward_cnt` INT DEFAULT NULL,
                          `social_id` VARCHAR(10) DEFAULT NULL,
                          `auth_vender` VARCHAR(10) DEFAULT NULL,
                          `invest_type` ENUM('CONSERVATIVE', 'MODERATE', 'AGGRESSIVE') DEFAULT NULL,
                          `last_visit` DATETIME DEFAULT NULL,
                          `email` VARCHAR(50) DEFAULT NULL,
                          `password` VARCHAR(255) DEFAULT NULL,
                          `benefit` TINYINT(1) DEFAULT NULL,
                          `nickname` VARCHAR(20) DEFAULT NULL,
                          PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;