# --- !Ups

set ignorecase true;

create table "questions" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "name" VARCHAR(254) NOT NULL,
  "description" VARCHAR(1024) NOT NULL
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
  "datecrea" VARCHAR(256),
  "note" INT 
);



# --- !Downs

drop table answers;
drop table part;
drop table responses;

