# accouning schema

# --- !Ups
create table tradings (
    id int unsigned auto_increment not null comment 'サロゲートキー',
    account_id int unsigned not null comment '科目ID',
    traded date not null comment '取引日',
    name varchar(20) comment '取引名',
    means varchar(20) comment '取引手段',
    payment_due_date datetime comment '支払予定日',
    summary varchar(100) comment '摘要',
    suppliers varchar(50) comment '取引先',
    payment int unsigned not null comment '金額',
    distribution_ratios smallint unsigned comment '按分率',
    created datetime not null comment '登録日時',
    modified datetime not null comment '修正日時',
    foreign key (account_id) references accounts(id),
    primary key (id)
) comment = '取引';

# --- !Downs
drop table tradings;
