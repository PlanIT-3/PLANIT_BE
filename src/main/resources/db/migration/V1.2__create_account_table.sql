create table plan_it.account
(
    account_id            bigint auto_increment
        primary key,
    member_id             bigint                                   not null,
    account_name          varchar(255)                             not null,
    account_number        varchar(50)                              not null,
    account_currency      varchar(10)    default 'KRW'             not null,
    account_balance       decimal(20, 2) default 0.00              not null,
    account_deposit       decimal(20, 2) default 0.00              not null,
    earnings_rate         decimal(5, 4)  default 0.0000            null,
    account_invested_cost decimal(20, 2) default 0.00              not null,
    last_tran_date        timestamp                                null,
    is_deleted            tinyint(1)     default 0                 not null,
    created_at            timestamp      default CURRENT_TIMESTAMP null,
    updated_at            timestamp      default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    account_start_date    timestamp                                null,
    account_end_date      timestamp                                null,
    is_integrated         tinyint(1)     default 0                 not null,
    constraint account_number
        unique (account_number)
);