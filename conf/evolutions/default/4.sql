# accouning schema

# --- !Ups
create table favorite_tradings (
    id int unsigned auto_increment not null comment 'サロゲートキー',
    keyword varchar(20) not null comment 'キーワード',
    account_id int unsigned not null comment '科目ID',
    trading_means_id int unsigned not null comment '取引手段ID',
    summary varchar(100) comment '摘要',
    suppliers varchar(50) comment '取引先',
    payment int unsigned comment '金額',
    distribution_ratios smallint unsigned comment '按分率',
    created datetime not null comment '登録日時',
    modified datetime not null comment '修正日時',
    foreign key (account_id) references accounts(id),
    foreign key (trading_means_id) references trading_means(id),
    primary key (id)
) comment = 'よく使う取引';

# --- !Downs
drop table favorite_tradings;
