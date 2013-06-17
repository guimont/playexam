# --- !Ups

create table if not exists "answers" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "name" VARCHAR(254) NOT NULL,
  "description" VARCHAR(1024) NOT NULL
);

# --- !Downs

drop table answers;

