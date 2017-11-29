package daos

import javax.inject.{Inject, Singleton}
import models.entitys.BaseChatEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext

@Singleton
class BaseChatDao @Inject()(
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

    def title = column[Option[String]]("title")

    def * = (
      id,
      chat_type,
      title
    ).shaped <> ((BaseChatEntity.apply _).tupled, BaseChatEntity.unapply)
  }

}
