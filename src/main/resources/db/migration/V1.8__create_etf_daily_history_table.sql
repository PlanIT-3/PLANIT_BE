CREATE TABLE etf_daily_history (
    etf_daily_history_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    shorten_code               VARCHAR(20) NOT NULL COMMENT '단축코드 (FK → product.shorten_code)',
    invest_type                ENUM('SAFE','CONSERVATIVE','MODERATE','AGGRESSIVE','VERY_AGGRESSIVE') NULL COMMENT '투자 성향',
    base_date                  DATE NULL COMMENT '기준일자 (YYYYMMDD)',
    isin_code                  VARCHAR(20) NULL COMMENT 'ISIN 코드',
    item_name                  VARCHAR(100) NULL COMMENT '종목명',
    closing_price              INT NULL COMMENT '종가',
    `difference`               INT NULL COMMENT '대비 (전일 대비 등락)',
    fluctuation_rate           DECIMAL(20,3) NULL COMMENT '등락률',
    net_asset_value            DECIMAL(20,3) NULL COMMENT '순자산가치(NAV)',
    market_open_price          INT NULL COMMENT '시가',
    high_price                 INT NULL COMMENT '고가',
    low_price                  INT NULL COMMENT '저가',
    trade_quantity             BIGINT NULL COMMENT '거래량',
    trade_price                BIGINT NULL COMMENT '거래대금',
    market_total_amount        BIGINT NULL COMMENT '시가총액',
    stock_listing_count        BIGINT NULL COMMENT '상장주식수',
    base_index_name            VARCHAR(100) NULL COMMENT '기초지수명',
    base_index_closing_price   DECIMAL(20,3) NULL COMMENT '기초지수 종가',
    net_asset_total_amount     BIGINT NULL COMMENT '순자산총액',
    created_at                 TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

   CONSTRAINT fk_etf_history_product
       FOREIGN KEY (shorten_code)
           REFERENCES product (shorten_code)
           ON UPDATE CASCADE
           ON DELETE RESTRICT
)   COMMENT = 'ETF 일별 시세 히스토리';