# accouning schema

# --- !Ups
create table accounts (
    id int unsigned auto_increment not null comment 'サロゲートキー',
    name varchar(20) not null comment '科目名',
    primary key (id)
) comment = '科目';

# --- !Downs
drop table accounts;
