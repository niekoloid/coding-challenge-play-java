# --- !Ups
create table transaction (
  id                        bigint not null,
  amount                    double not null,
  type                      varchar(255) not null,
  parent_id                 bigint,
  constraint pk_transaction primary key (id)
 );

create sequence transaction_seq;


# --- !Downs
drop table transaction;

