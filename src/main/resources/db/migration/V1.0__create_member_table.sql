create table plan_it.member
(
    member_id    bigint auto_increment
        primary key,
    role         varchar(20)                                                        not null,
    connected_id varchar(40)                                                        null,
    reward_cnt   int                                                                null,
    social_id    varchar(10)                                                        null,
    auth_vender  varchar(10)                                                        null,
    invest_type  enum ('CONSERVATIVE', 'STABLE', 'AGGRESSIVE', 'GROWTH', 'NEUTRAL') null,
    last_visit   datetime                                                           null,
    email        varchar(50)                                                        null,
    password     varchar(255)                                                       null,
    benefit      tinyint(1)                                                         null,
    nickname     varchar(20)                                                        null,
    stable       double                    default 0                                null,
    income       double                    default 0                                null,
    liquid       double                    default 0                                null,
    growth       double                    default 0                                null,
    diversified  double                    default 0                                null,
    isa_type     enum ('GENERAL', 'RURAL') default 'GENERAL'                        not null comment 'ISA 유형: 일반형(200 한도), 농어촌형(400 한도)',
    fcm_token    varchar(255)                                                       null comment 'fcm token'
);
