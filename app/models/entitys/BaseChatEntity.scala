package models.entitys

case class BaseChatEntity (
                            id: Long,
                            chat_type: String,
                            title: Option[String] = None
                          )
