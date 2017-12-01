package models

import models.entitys.UserEntity

case class BMT1(
                 message_id: Long,
                 from: Option[UserEntity] = None,
                 date: Long,
                 chat: BaseChatModel,
                 forward_from: Option[UserEntity] = None,
                 forward_from_chat: Option[BaseChatModel] = None,
                 forward_from_message_id: Option[Long] = None,
                 forward_signature: Option[String] = None,
                 forward_date: Option[Long] = None,
                 edit_date: Option[Long] = None,
                 author_signature: Option[String] = None,
                 text: Option[String] = None,
                 left_chat_member: Option[UserEntity] = None
               )
