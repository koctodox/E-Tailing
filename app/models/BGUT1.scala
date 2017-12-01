package models

case class BGUT1 (
                   update_id: Long,
                   message: Option[BMT1] = None,
                   edited_message: Option[BMT1] = None,
                   channel_post: Option[BMT1] = None,
                   edited_channel_post: Option[BMT1] = None
                 )
