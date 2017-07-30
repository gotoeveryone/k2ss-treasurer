# accouning schema

# --- !Ups
create table accounts (
    id int(10) unsigned auto_increment not null,
    name varchar(20) not null,
    created datetime not null,
    modified datetime not null,
    primary key (id)
);

# --- !Downs
drop table accounts;
