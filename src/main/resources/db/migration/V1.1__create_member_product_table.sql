create table member_product
(
    member_product_id  bigint auto_increment comment '사용자 상품 ID'
        primary key,
    member_id          bigint                               not null comment '회원 ID',
    product_id         varchar(20) charset utf8mb4          null,
    product_type_code  varchar(10)                          null comment '상품유형코드 (01:주식, 02:펀드, 03:CMA, 04:해외주식, 05:신탁/퇴직연금, 06:채권, 07:RP, 08:CD/CP, 09:ELS/DLS, 10:해외무추얼펀드, 11:Wrap, 12:외화RP, 13:연금저축, 14:선물옵션, 99:기타)',
    account_extends    varchar(50)                          null comment '계좌번호 확장',
    product_type       varchar(100)                         null comment '상품유형',
    avg_present_amount decimal(15, 4)                       null comment '평균매입가',
    present_amount     decimal(15, 4)                       null comment '현재가',
    balance_type       varchar(20)                          null comment '잔고유형',
    item_name          varchar(200)                         null comment '상품/종목명',
    valuation_pl       decimal(15, 4)                       null comment '평가손익',
    valuation_amount   decimal(15, 4)                       null comment '평가금액',
    quantity           decimal(15, 4)                       null comment '수량',
    purchase_amount    decimal(15, 4)                       null comment '매입금액',
    earnings_rate      decimal(8, 4)                        null comment '수익률(%)',
    account_currency   varchar(3)                           null comment '통화코드',
    settle_quantity    decimal(15, 4)                       null comment '정산수량',
    item_code          varchar(50)                          null comment '상품/종목코드',
    deposit_received   decimal(15, 4)                       null comment '예수금',
    is_integrated      tinyint(1) default 0                 null comment 'codef 연동 여부',
    created_at         timestamp  default CURRENT_TIMESTAMP null comment '생성일시',
    updated_at         timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '수정일시',
    is_deleted         tinyint(1)                           null,
    account_id         bigint                               null
)
    comment '사용자 보유 상품' collate = utf8mb4_unicode_ci;