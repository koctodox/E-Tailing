package models

import models.entitys.UserEntity

case class BaseLoopMessageModel(
                               message_id: Long,
                               from: Option[UserEntity] = None,
                               date: Long,
                               chat: BaseChatModel,
                               text: Option[String] = None
                               )
