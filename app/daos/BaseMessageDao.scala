package daos

import javax.inject.{Inject, Singleton}

import models.entitys.BaseMessageEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BaseMessageDao @Inject()(
                                val userDao: UserDao,
                                val baseChatDao: BaseChatDao,
                                protected val dbConfigProvider: DatabaseConfigProvider
                              )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  val baseMessageTableQuery = TableQuery[BaseMessageTable]

  def upsert(baseMessageEntity: BaseMessageEntity): Future[Int] = {
    db.run(baseMessageTableQuery.insertOrUpdate(baseMessageEntity))
  }

  @Singleton
  final class BaseMessageTable(tag: Tag) extends Table[BaseMessageEntity](tag, "BASE_MESSAGE") {

    def message_id = column[Long]("message_id", O.PrimaryKey)

    //User FK
    def from_id = column[Option[Long]]("from_id")

    def date = column[Long]("date")

    //BaseChat FK
    def chat_id = column[Long]("chat_id")

    //User FK
    def forward_from_id = column[Option[Long]]("forward_from_id")

    //BaseChat FK
    def forward_from_chat_id = column[Option[Long]]("forward_from_chat_id")

    def forward_from_message_id = column[Option[Long]]("forward_from_message_id")

    def forward_signature = column[Option[String]]("forward_signature")

    def forward_date = column[Option[Long]]("forward_date")

    //BaseMessage FK
    def reply_to_message_id = column[Option[Long]]("reply_to_message_id")

    def edit_date = column[Option[Long]]("edit_date")

    def author_signature = column[Option[String]]("author_signature")

    def text = column[Option[String]]("text")

    //User FK
    def left_chat_member_id = column[Option[Long]]("left_chat_member_id")

    //BaseMessage FK
    def pinned_message_id = column[Option[Long]]("pinned_message_id")

    def * = (
      message_id,
      from_id,
      date,
      chat_id,
      forward_from_id,
      forward_from_chat_id,
      forward_from_message_id,
      forward_signature,
      forward_date,
      reply_to_message_id,
      edit_date,
      author_signature,
      text,
      left_chat_member_id,
      pinned_message_id
    ).shaped <> ((BaseMessageEntity.apply _).tupled, BaseMessageEntity.unapply)

    def from_fk = foreignKey("FK_FromUserId_BaseMessage_Users", from_id, userDao.userTableQuery)(_.id.?)

    def chat_fk = foreignKey("FK_ChatId_BaseMessage_BaseChat", chat_id, baseChatDao.baseChatTableQuery)(_.id)

    def forward_from_fk = foreignKey("FK_ForwardFromId_BaseMessage_Users", forward_from_id, userDao.userTableQuery)(_.id.?)

    def forward_from_chat_fk = foreignKey("FK_ForwardFromChatId_BaseMessage_BaseChat", forward_from_chat_id, baseChatDao.baseChatTableQuery)(_.id.?)

    def reply_to_message_fk = foreignKey("FK_ReplyToMessageId_BaseMessage_BaseMessage", reply_to_message_id, baseMessageTableQuery)(_.message_id.?)

    def left_chat_member_fk = foreignKey("FK_LeftChatMemberId_BaseMessage_Users", left_chat_member_id, userDao.userTableQuery)(_.id.?)

    def pinned_message_fk = foreignKey("FK_PinnedMessageId_BaseMessage_BaseMessage", pinned_message_id, baseMessageTableQuery)(_.message_id.?)

  }

}
