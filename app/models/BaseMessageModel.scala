package models

import models.entitys.UserEntity

case class BaseMessageModel(
                             message_id: Long,
                             from: Option[UserEntity] = None,
                             date: Long,
                             chat: BaseChatModel,
                             forward_from: Option[UserEntity] = None,
                             forward_from_chat: Option[BaseChatModel] = None,
                             forward_from_message_id: Option[Long] = None,
                             forward_signature: Option[String] = None,
                             forward_date: Option[Long] = None,
                             reply_to_message: Option[BaseMessageModel] = None,
                             edit_date: Option[Long] = None,
                             author_signature: Option[String] = None,
                             text: Option[String] = None,
                             left_chat_member: Option[UserEntity] = None,
                             pinned_message: Option[BaseMessageModel] = None
                           )

//More helps in:
//https://core.telegram.org/bots/api#message
