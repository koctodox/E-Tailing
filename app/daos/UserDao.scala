package daos

import javax.inject.{Inject, Singleton}

import models.entitys.UserEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDao @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                       )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  val userTableQuery = TableQuery[UserTable]

  def upsert (userEntity: UserEntity): Future[Int] = {
    db.run(userTableQuery.insertOrUpdate(userEntity))
  }

  @Singleton
  final class UserTable(tag: Tag) extends Table[UserEntity](tag, "USERS") {
    def id = column[Long]("id", O.PrimaryKey)

    def is_bot = column[Boolean]("is_bot")

    def first_name = column[String]("first_name")

    def last_name = column[Option[String]]("last_name")

    def username = column[Option[String]]("username")

    def language_code = column[Option[String]]("language_code")

    def * =(
      id,
      is_bot,
      first_name,
      last_name,
      username,
      language_code
    ).shaped <> ((UserEntity.apply _).tupled, UserEntity.unapply)

  }

}
