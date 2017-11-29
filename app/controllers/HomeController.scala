package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(
                                implicit val ex: ExecutionContext
                              ) extends Controller {

  def index = Action {
    Ok("Server is up ...")
  }

}
