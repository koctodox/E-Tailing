package controllers

import javax.inject._
import daos.UserDao
import play.api.mvc._

@Singleton
class HomeController @Inject()(userDao: UserDao) extends Controller {

  def index = Action {

    Ok("Server is Up...")
  }

}
