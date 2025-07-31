CREATE TABLE IF NOT EXISTS product (
                         srtn_cd        VARCHAR(20)  NOT NULL, -- 단축코드
                         isin_cd        VARCHAR(30)  NOT NULL, -- ISIN 코드
                         itms_nm        VARCHAR(100) NOT NULL, -- 종목명
                         bas_dt         VARCHAR(8)   NOT NULL, -- 기준일자 (YYYYMMDD)

                         clpr           VARCHAR(20),           -- 종가
                         vs             VARCHAR(20),           -- 전일 대비
                         flt_rt         VARCHAR(20),           -- 등락률
                         mkp            VARCHAR(20),           -- 시가
                         hipr           VARCHAR(20),           -- 고가
                         lopr           VARCHAR(20),           -- 저가
                         trqu           VARCHAR(30),           -- 거래량
                         tr_prc         VARCHAR(30),           -- 거래대금
                         lstg_st_cnt    VARCHAR(30),           -- 상장주식수
                         mrkt_tot_amt   VARCHAR(30),           -- 시가총액

                         risk_level     VARCHAR(20),           -- 투자위험성

                         PRIMARY KEY (srtn_cd, bas_dt)         -- 복합키: 종목 + 날짜 기준으로 유니크
);
