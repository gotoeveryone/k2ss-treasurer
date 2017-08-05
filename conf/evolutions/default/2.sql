# accouning schema

# --- !Ups
create table trading_means (
    id int unsigned auto_increment not null comment 'サロゲートキー',
    name varchar(20) not null comment '取引手段名',
    primary key (id)
) comment = '取引手段';

# --- !Downs
drop table trading_means;
