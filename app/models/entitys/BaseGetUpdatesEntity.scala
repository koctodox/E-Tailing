package models.entitys

case class BaseGetUpdatesEntity (
                                update_id: Long,
                                message_id: Option[Long],
                                edited_message_id: Option[Long],
                                channel_post_id: Option[Long],
                                edited_channel_post_id: Option[Long]
                                )
