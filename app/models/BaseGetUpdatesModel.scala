package models

case class BaseGetUpdatesModel(
                       update_id: Long,
                       message: Option[BaseMessageModel] = None,
                       edited_message: Option[BaseMessageModel] = None,
                       channel_post: Option[BaseMessageModel] = None,
                       edited_channel_post: Option[BaseMessageModel] = None
                     )
//More helps in:
//https://core.telegram.org/bots/api#getting-updates
