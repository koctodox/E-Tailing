package controllers

import javax.inject._

import daos.UserDao
import models.User
import play.api.mvc._

@Singleton
class HomeController @Inject()(userDao: UserDao) extends Controller {

  def index = Action {
    val user = User(1,true,first_name = "Kian")
    userDao.insert(user)
    Ok("Server is Up...")
  }

}
