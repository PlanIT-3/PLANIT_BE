create table goal
(
    goal_id       bigint auto_increment     primary key,
    member_id     bigint       not null,
    object_name   varchar(255) not null,
    target_amount bigint       not null,
    start_date    date         not null,
    end_date      date         not null,
    deposit_rate  int          not null,
    isa_rate      int          not null,
    start_amount  bigint       not null,
    goal_rate     int          not null,
    constraint fk_object_user_id
        foreign key (member_id) references plan_it.member (member_id)
            on delete cascade
);
