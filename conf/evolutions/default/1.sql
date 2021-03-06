# --- !Ups

set ignorecase true;

create table "tests" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "name" VARCHAR(254) NOT NULL,
  "nb_q" BIGINT NOT NULL,
  "delay" BIGINT NOT NULL
  );

create table "questions" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "qid" BIGINT NOT NULL,
  "tid" BIGINT NOT NULL,
  "description" VARCHAR(1024) NOT NULL,
  "open" BOOLEAN
);

create table "parts" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "qId" BIGINT NOT NULL,
  "part" VARCHAR(1024) NOT NULL
  );

create table "answers" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "qId" BIGINT NOT NULL,
  "answer" VARCHAR(1024) NOT NULL,
  "check" BOOLEAN
);

create table "results" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "qId" BIGINT NOT NULL,
  "eid" BIGINT NOT NULL,
  "response" VARCHAR(1024) NOT NULL,
  "note" FLOAT
);

create table "responses" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "qId" BIGINT NOT NULL,
  "tid" BIGINT NOT NULL,
  "open" BOOLEAN,
  "response" VARCHAR(1024) NOT NULL,
);

create table "candidates" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "datecrea" VARCHAR(256),
  "firstname" VARCHAR(256) NOT NULL,
  "lastname" VARCHAR(256) NOT NULL
);
  

create table "exams"  (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "cid" BIGINT NOT NULL,
  "tid" BIGINT NOT NULL,
  "token" VARCHAR(256),
  "datecrea" VARCHAR(256),
  "notifier" VARCHAR(256),
  "note" FLOAT 
);


create table "users"  (
  "email" VARCHAR(256),
  "name" VARCHAR(256),
  "password" VARCHAR(256)
);


# --- !Downs

drop table answers;
drop table part;
drop table questions;
drop table results;
drop table candidates;
drop table exams;
