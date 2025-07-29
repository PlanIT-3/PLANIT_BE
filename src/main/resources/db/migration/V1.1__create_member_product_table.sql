CREATE TABLE member_product (
                                member_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                member_id BIGINT NOT NULL,
                                product_type_code VARCHAR(2) NOT NULL,
                                present_amount DECIMAL(15,2) DEFAULT 0.00,
                                quantity INT DEFAULT 0,
                                item_name VARCHAR(255) NOT NULL,
                                item_code VARCHAR(100) NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);