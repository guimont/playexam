# --- !Ups

create table if not exists "questions" (
  "id" BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  "name" VARCHAR(254) NOT NULL,
  "description" VARCHAR(1024) NOT NULL
);


create table if not exists "responses" (
  "responseId" BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  "answerId" BIGINT NOT NULL,
  "response" VARCHAR(1024) NOT NULL
);


# --- !Downs

drop table answers;
drop table responses;

