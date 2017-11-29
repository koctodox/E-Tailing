package daos

import javax.inject.{Inject, Singleton}
import models.entitys.BaseGetUpdatesEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext

@Singleton
class BaseGetUpdatesDao @Inject()(
                                   val baseMessageDao: BaseMessageDao,
                                   protected val dbConfigProvider: DatabaseConfigProvider
                                 )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val baseGetUpdatesTableQuery = TableQuery[BaseGetUpdatesTable]

  def insert(baseGetUpdatesEntity: BaseGetUpdatesEntity) = {
    db.run(baseGetUpdatesTableQuery returning baseGetUpdatesTableQuery.map(_.update_id) += baseGetUpdatesEntity)
  }

  @Singleton
  final class BaseGetUpdatesTable(tag: Tag) extends Table[BaseGetUpdatesEntity](tag, "BASE_GET_UPDATES") {

    def update_id = column[Long]("update_id", O.PrimaryKey)

    //BASE_MESSAGE FK
    def message_id = column[Option[Long]]("message_id")

    //BASE_MESSAGE FK
    def edited_message_id = column[Option[Long]]("edited_message_id")

    //BASE_MESSAGE FK
    def channel_post_id = column[Option[Long]]("channel_post_id")

    //BASE_MESSAGE FK
    def edited_channel_post_id = column[Option[Long]]("edited_channel_post_id")

    def * = (
      update_id,
      message_id,
      edited_message_id,
      channel_post_id,
      edited_channel_post_id
    ).shaped <> ((BaseGetUpdatesEntity.apply _).tupled, BaseGetUpdatesEntity.unapply)

    def message_fk = foreignKey("FK_MessageId_BaseGetUpdates_BaseMessage", message_id, baseMessageDao.baseMessageTableQuery)(_.message_id.?)

    def edited_message_fk = foreignKey("FK_EditMessageId_BaseGetUpdates_BaseMessage", edited_message_id, baseMessageDao.baseMessageTableQuery)(_.message_id.?)

    def channel_post_fk = foreignKey("FK_ChannelPostId_BaseGetUpdates_BaseMessage", channel_post_id, baseMessageDao.baseMessageTableQuery)(_.message_id.?)

    def edited_channel_post_fk = foreignKey("FK_EditedChannelId_BaseGetUpdates_BaseMessage", edited_channel_post_id, baseMessageDao.baseMessageTableQuery)(_.message_id.?)

  }

}
