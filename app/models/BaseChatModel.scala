package models

case class BaseChatModel(
                     id: Long,
                     chat_type: String,
                     pinned_message: Option[BaseMessageModel] = None
                   )
//More helps in:
//https://core.telegram.org/bots/api#chat
