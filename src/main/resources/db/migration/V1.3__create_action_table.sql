create table plan_it.action
(
    action_id              bigint auto_increment
        primary key,
    account_id             bigint                               null,
    goal_id                bigint                               not null,
    account_number         varchar(50)                          null,
    member_product_id      bigint                               null,
    account_allocated_rate int                                  null,
    account_type           enum ('ISA', 'DEPOSIT')              null,
    amount                 int                                  null,
    is_deleted             tinyint(1) default 0                 not null,
    created_at             datetime   default CURRENT_TIMESTAMP not null,
    updated_at             datetime   default CURRENT_TIMESTAMP null
);