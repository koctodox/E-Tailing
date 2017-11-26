package models.entitys

case class BaseMessageEntity(
                              message_id: Long,
                              from_id: Option[Long] = None,
                              date: Long,
                              chat_id: Long,
                              forward_from_id: Option[Long] = None,
                              forward_from_chat_id: Option[Long] = None,
                              forward_from_message_id: Option[Long] = None,
                              forward_signature: Option[String] = None,
                              forward_date: Option[Long] = None,
                              reply_to_message_id: Option[Long] = None,
                              edit_date: Option[Long] = None,
                              author_signature: Option[String] = None,
                              text: Option[String] = None,
                              left_chat_member_id: Option[Long] = None,
                              pinned_message_id: Option[Long] = None
                            )