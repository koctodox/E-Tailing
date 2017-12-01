package controllers

import javax.inject.Inject
import models._
import play.api.libs.functional.syntax._
import models.entitys.UserEntity
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import services.GetUpdatesService
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class GetUpdatesController @Inject()(
                                      ws: WSClient,
                                      getUpdatesService: GetUpdatesService
                                    )(implicit val ex: ExecutionContext) extends Controller {

  // TODO => must user application.conf here (in baseURL)
  val baseUrl: String = "https://api.telegram.org/bot500464749:AAGjbwOZfYLXcdZ6ue-7WtCB3mBSR5jbgDA"

  def getUpdates = Action.async {
        val method = "/getupdates"
        val URL = s"$baseUrl$method"
        val request = ws.url(URL)

    request.get map { response =>
      response.json.validate[GetResults] match {
        // TODO => must use Logger.warn here (in case error) [log the json]
        case jsError: JsError =>
          println(Console.RED ,"Errors: " + jsError + response.json)
        case jsSuccess: JsSuccess[GetResults] =>
          (jsSuccess.get.ok) match {
            // TODO => must use Logger.warn here (in case false)
            case false =>
              println(Console.RED,"Error: OK is equal false")
            case true =>
              getUpdatesService.getUpdates_MOST_DELETED(jsSuccess.get.result)
              println(s"your operation finished")
          }
      }
      Ok(s"In get updates method")
    }
  }

  // """""" JSON Deserialazation : """"""

  implicit val user_EntityReader: Reads[UserEntity] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "is_bot").read[Boolean] and
      (JsPath \ "first_name").read[String] and
      (JsPath \ "last_name").readNullable[String] and
      (JsPath \ "username").readNullable[String] and
      (JsPath \ "language_code").readNullable[String]
    ) (UserEntity.apply _)

  implicit val baseChat_ModelReader: Reads[BaseChatModel] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "type").read[String] and
      (JsPath \ "title").readNullable[String]
    ) (BaseChatModel.apply _)

//  implicit val baseMessage_ModelReader: Reads[BaseMessageModel] = (
//    (JsPath \ "message_id").read[Long] and
//      (JsPath \ "from").readNullable[UserEntity] and
//      (JsPath \ "date").read[Long] and
//      (JsPath \ "chat").read[BaseChatModel] and
//      (JsPath \ "forward_from").readNullable[UserEntity] and
//      (JsPath \ "forward_from_chat").readNullable[BaseChatModel] and
//      (JsPath \ "forward_from_message_id").readNullable[Long] and
//      (JsPath \ "forward_signature").readNullable[String] and
//      (JsPath \ "forward_date").readNullable[Long] and
//      (JsPath \ "reply_to_message").readNullable[BaseLoopMessageModel] and
//      (JsPath \ "edit_date").readNullable[Long] and
//      (JsPath \ "author_signature").readNullable[String] and
//      (JsPath \ "text").readNullable[String] and
//      (JsPath \ "left_chat_member").readNullable[UserEntity] and
//      (JsPath \ "pinned_message").readNullable[BaseLoopMessageModel]
//    ) (BaseMessageModel.apply _)

  implicit val BMIT1_ModelReader: Reads[BMT1] = (
    (JsPath \ "message_id").read[Long] and
      (JsPath \ "from").readNullable[UserEntity] and
      (JsPath \ "date").read[Long] and
      (JsPath \ "chat").read[BaseChatModel] and
      (JsPath \ "forward_from").readNullable[UserEntity] and
      (JsPath \ "forward_from_chat").readNullable[BaseChatModel] and
      (JsPath \ "forward_from_message_id").readNullable[Long] and
      (JsPath \ "forward_signature").readNullable[String] and
      (JsPath \ "forward_date").readNullable[Long] and
      (JsPath \ "edit_date").readNullable[Long] and
      (JsPath \ "author_signature").readNullable[String] and
      (JsPath \ "text").readNullable[String] and
      (JsPath \ "left_chat_member").readNullable[UserEntity]
    ) (BMT1.apply _)

  implicit val baseLoopMessage_ModelReader: Reads[BaseLoopMessageModel] = (
    (JsPath \ "message_id").read[Long] and
      (JsPath \ "from").readNullable[UserEntity] and
      (JsPath \ "date").read[Long] and
      (JsPath \ "chat").read[BaseChatModel] and
      (JsPath \ "text").readNullable[String]
    ) (BaseLoopMessageModel.apply _)

//  implicit val getUpdates_ModelReader: Reads[BaseGetUpdatesModel] = (
//    (JsPath \ "update_id").read[Long] and
//      (JsPath \ "message").readNullable[BaseMessageModel] and
//      (JsPath \ "edited_message").readNullable[BaseMessageModel] and
//      (JsPath \ "channel_post").readNullable[BaseMessageModel] and
//      (JsPath \ "edited_channel_post").readNullable[BaseMessageModel]
//    ) (BaseGetUpdatesModel.apply _)

  implicit val BGUT1_ModelReader: Reads[BGUT1] = (
    (JsPath \ "update_id").read[Long] and
      (JsPath \ "message").readNullable[BMT1] and
      (JsPath \ "edited_message").readNullable[BMT1] and
      (JsPath \ "channel_post").readNullable[BMT1] and
      (JsPath \ "edited_channel_post").readNullable[BMT1]
    ) (BGUT1.apply _)

//  implicit val getResult_reader: Reads[GetResults] = (
//    (JsPath \ "ok").read[Boolean] and
//      (JsPath \ "result").read[Seq[BaseGetUpdatesModel]]
//    ) (GetResults.apply _)

  implicit val getResult_reader: Reads[GetResults] = (
    (JsPath \ "ok").read[Boolean] and
      (JsPath \ "result").read[Seq[BGUT1]]
    ) (GetResults.apply _)
}
