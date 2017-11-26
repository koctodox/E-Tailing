# --- !Ups

create table "BASE_MESSAGE"(
"message_id" BIGINT NOT NULL PRIMARY KEY,
"from_id" BIGINT ,
"date" BIGINT,
"chat_id" BIGINT,
"forward_from_id" BIGINT,
"forward_from_chat_id" BIGINT,
"forward_from_message_id" BIGINT,
"forward_signature" BIGINT,
"forward_date" BIGINT,
"reply_to_message_id" BIGINT,
"edit_date" BIGINT ,
"author_signature" VARCHAR(32),
"text" VARCHAR,
"left_chat_member_id" BIGINT,
"pinned_message_id" BIGINT
);

create table "BASE_CHAT"(
"id" BIGINT NOT NULL PRIMARY KEY,
"chat_type" VARCHAR(16) ,
"pinned_message" BIGINT
);

alter table "BASE_CHAT" add constraint "FK_PinnedMessageId_BaseMessage_BaseChat" foreign key("pinned_message") references "BASE_MESSAGE"("message_id") on update NO ACTION on delete NO ACTION;

alter table "BASE_MESSAGE" add constraint "FK_FromUserId_BaseMessage_Users" foreign key("from_id") references "USERS"("id") on update NO ACTION on delete NO ACTION;

alter table "BASE_MESSAGE" add constraint "FK_ChatId_BaseMessage_BaseChat" foreign key("chat_id") references "BASE_CHAT"("id") on update NO ACTION on delete NO ACTION;

alter table "BASE_MESSAGE" add constraint "FK_ForwardFromId_BaseMessage_Users" foreign key("forward_from_id") references "USERS"("id") on update NO ACTION on delete NO ACTION;

alter table "BASE_MESSAGE" add constraint "FK_ForwardFromChatId_BaseMessage_BaseChat" foreign key("forward_from_chat_id") references "BASE_CHAT"("id") on update NO ACTION on delete NO ACTION;

alter table "BASE_MESSAGE" add constraint "FK_ReplyToMessageId_BaseMessage_BaseMessage" foreign key("reply_to_message_id") references "BASE_MESSAGE"("message_id") on update NO ACTION on delete NO ACTION;

alter table "BASE_MESSAGE" add constraint "FK_LeftChatMemberId_BaseMessage_Users" foreign key("left_chat_member_id") references "USERS"("id") on update NO ACTION on delete NO ACTION;

alter table "BASE_MESSAGE" add constraint "FK_PinnedMessageId_BaseMessage_BaseMessage" foreign key("pinned_message_id") references "BASE_MESSAGE"("message_id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "BASE_MESSAGE" drop constraint "FK_PinnedMessageId_BaseMessage_BaseMessage";

alter table "BASE_MESSAGE" drop constraint "FK_LeftChatMemberId_BaseMessage_Users";

alter table "BASE_MESSAGE" drop constraint "FK_ReplyToMessageId_BaseMessage_BaseMessage";

alter table "BASE_MESSAGE" drop constraint "FK_ForwardFromChatId_BaseMessage_BaseChat";

alter table "BASE_MESSAGE" drop constraint "FK_ForwardFromId_BaseMessage_Users";

alter table "BASE_MESSAGE" drop constraint "FK_ChatId_BaseMessage_BaseChat";

alter table "BASE_MESSAGE" drop constraint "FK_FromUserId_BaseMessage_Users";

alter table "BASE_CHAT" drop constraint "FK_PinnedMessageId_BaseMessage_BaseChat";

drop table "BASE_CHAT";

drop table "BASE_MESSAGE";
