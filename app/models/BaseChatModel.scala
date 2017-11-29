package models

case class BaseChatModel(
                     id: Long,
                     chat_type: String,
                     title: Option[String] = None
                   )
//More helps in:
//https://core.telegram.org/bots/api#chat
