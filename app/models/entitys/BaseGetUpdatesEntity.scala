package models.entitys

case class BaseGetUpdatesEntity (
                                update_id: Long,
                                message_id: Option[Long] = None,
                                edited_message_id: Option[Long] = None,
                                channel_post_id: Option[Long] = None,
                                edited_channel_post_id: Option[Long] = None
                                )
