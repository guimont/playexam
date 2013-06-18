# --- !Ups

create table if not exists "answers" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "name" VARCHAR(254) NOT NULL,
  "description" VARCHAR(1024) NOT NULL
);


create table if not exists "responses" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "answerId" BIGINT NOT NULL,
  "resp" VARCHAR(1024),
  "selected" BOOLEAN
);

# --- !Downs

drop table answers;

