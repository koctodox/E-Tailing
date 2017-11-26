# --- !Ups

create table "BASE_GET_UPDATES"(
"update_id" BIGINT NOT NULL PRIMARY KEY,
"message_id" BIGINT ,
"edited_message_id" BIGINT,
"channel_post_id" BIGINT,
"edited_channel_post_id" BIGINT
);

alter table "BASE_GET_UPDATES" add constraint "FK_MessageId_BaseGetUpdates_BaseMessage" foreign key("message_id") references "BASE_MESSAGE"("message_id") on update NO ACTION on delete NO ACTION;

alter table "BASE_GET_UPDATES" add constraint "FK_EditMessageId_BaseGetUpdates_BaseMessage" foreign key("edited_message_id") references "BASE_MESSAGE"("message_id") on update NO ACTION on delete NO ACTION;

alter table "BASE_GET_UPDATES" add constraint "FK_ChannelPostId_BaseGetUpdates_BaseMessage" foreign key("channel_post_id") references "BASE_MESSAGE"("message_id") on update NO ACTION on delete NO ACTION;

alter table "BASE_GET_UPDATES" add constraint "FK_EditedChannelId_BaseGetUpdates_BaseMessage" foreign key("edited_channel_post_id") references "BASE_MESSAGE"("message_id") on update NO ACTION on delete NO ACTION;

# --- !Downs


alter table "BASE_MESSAGE" drop constraint "FK_EditedChannelId_BaseGetUpdates_BaseMessage";

alter table "BASE_MESSAGE" drop constraint "FK_ChannelPostId_BaseGetUpdates_BaseMessage";

alter table "BASE_MESSAGE" drop constraint "FK_EditMessageId_BaseGetUpdates_BaseMessage";

alter table "BASE_MESSAGE" drop constraint "FK_MessageId_BaseGetUpdates_BaseMessage";

drop table "BASE_GET_UPDATES";
