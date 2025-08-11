-- 1) 기존 복합 PK 제거
ALTER TABLE product
DROP PRIMARY KEY;

-- 2) 컬럼명 & 타입 변경
ALTER TABLE product
  CHANGE COLUMN srtn_cd       shorten_code            VARCHAR(20)     NOT NULL  COMMENT '단축 코드',
  CHANGE COLUMN bas_dt        base_date               DATE            NULL      COMMENT '기준일자',
  CHANGE COLUMN isin_cd       isin_code               VARCHAR(20)     NULL      COMMENT 'ISIN 코드',
  CHANGE COLUMN itms_nm       item_name               VARCHAR(100)    NULL      COMMENT '종목명',
  CHANGE COLUMN clpr          closing_price           INT             NULL      COMMENT '종가',
  CHANGE COLUMN vs            `difference`            INT             NULL      COMMENT '대비(전일 대비 등락가)',
  CHANGE COLUMN flt_rt        fluctuation_rate        DECIMAL(20,3)   NULL      COMMENT '등락률',
  CHANGE COLUMN mkp           market_open_price       INT             NULL      COMMENT '시가',
  CHANGE COLUMN hipr          high_price              INT             NULL      COMMENT '고가',
  CHANGE COLUMN lopr          low_price               INT             NULL      COMMENT '저가',
  CHANGE COLUMN trqu          trade_quantity          BIGINT          NULL      COMMENT '거래량',
  CHANGE COLUMN tr_prc        trade_price             BIGINT          NULL      COMMENT '거래대금',
  CHANGE COLUMN lstg_st_cnt   stock_listing_count     BIGINT          NULL      COMMENT '상장주식수',
  CHANGE COLUMN mrkt_tot_amt  market_total_amount     BIGINT          NULL      COMMENT '시가총액';

-- 3) 누락된 컬럼 추가
ALTER TABLE product
  ADD COLUMN net_asset_value          DECIMAL(20,3)  NULL COMMENT '순자산가치(NAV)',
  ADD COLUMN base_index_name          VARCHAR(100)   NULL COMMENT '기초지수명',
  ADD COLUMN base_index_closing_price DECIMAL(20,3)  NULL COMMENT '기초지수 종가',
  ADD COLUMN net_asset_total_amount   BIGINT          NULL COMMENT '순자산총액';

-- 4) 새 단일 PK 설정
ALTER TABLE product
  -- 1) 기존 PRIMARY KEY(product_id) 제거
  DROP PRIMARY KEY,
  -- 2) shorten_code 를 새 PRIMARY KEY 로 추가
  ADD PRIMARY KEY (shorten_code),
  -- 3) product_id 에 자동증가를 유지하기 위한 일반 INDEX 추가
  ADD KEY idx_product_id (product_id);
