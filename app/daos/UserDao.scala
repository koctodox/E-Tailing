package daos

import javax.inject.{Inject, Singleton}
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext

@Singleton
class UserDao @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                       )(implicit val ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  val userTableQuery = TableQuery[UserTable]

  def insert (user: User) = {
    println(s" compile heare .. user dao $user")

    db.run(userTableQuery returning userTableQuery.map(_.id) += user)
  }

  @Singleton
  final class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

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
    ).shaped <> ((User.apply _).tupled, User.unapply)

  }
}
