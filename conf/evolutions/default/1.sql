# --- !Ups

create table "USERS"("id" BIGSERIAL NOT NULL PRIMARY KEY,"is_bot" BOOLEAN ,"first_name" VARCHAR(32) ,"last_name" VARCHAR(32), "username" VARCHAR(32), "language_code" VARCHAR(10));

# --- !Downs

drop table "USERS";
