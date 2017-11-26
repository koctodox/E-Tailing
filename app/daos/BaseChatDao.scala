package daos

import javax.inject.{Inject, Singleton}
import models.entitys.BaseChatEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext

@Singleton
class BaseChatDao @Inject()(
                             val baseMessageDao: BaseMessageDao,
                             protected val dbConfigProvider: DatabaseConfigProvider
                           )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  val baseChatTableQuery = TableQuery[BaseChatTable]

  def insert (baseChatEntity: BaseChatEntity) = {
    db.run(baseChatTableQuery returning baseChatTableQuery.map(_.id) += baseChatEntity)
  }

  def findById (id: Long) = {
    db.run(baseChatTableQuery.filter(_.id === id).result)
  }

  @Singleton
  final class BaseChatTable(tag: Tag) extends Table[BaseChatEntity](tag, "BASE_CHAT") {
    def id = column[Long]("id", O.PrimaryKey)

    def chat_type = column[String]("chat_type")

    def pinned_message_id = column[Option[Long]]("pinned_message")

    def * = (
      id,
      chat_type,
      pinned_message_id
    ).shaped <> ((BaseChatEntity.apply _).tupled, BaseChatEntity.unapply)

    def pinned_message_fk = foreignKey("FK_PinnedMessageId_BaseMessage_BaseChat", pinned_message_id, baseMessageDao.baseMessageTableQuery)(_.message_id)
  }

}
