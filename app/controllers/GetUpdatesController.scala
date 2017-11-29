package controllers

import javax.inject.Inject

import models.{BaseChatModel, BaseGetUpdatesModel, BaseMessageModel, GetResults}
import play.api.libs.functional.syntax._
import models.entitys.UserEntity
import play.api.libs.json.{JsPath, Reads}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

class GetUpdatesController @Inject()(ws: WSClient)(implicit val ex: ExecutionContext) extends Controller {
  val baseUrl: String = "https://api.telegram.org/bot500464749:AAGjbwOZfYLXcdZ6ue-7WtCB3mBSR5jbgDA"

  def getUpdates = Action.async {
    val method = "/getupdates"
    val URL = s"$baseUrl$method"
    val request = ws.url(URL)

    request.get map { response =>
      val result = response.json.validate[GetResults].get
      Ok(s"this is the get update-method ... $result ")
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

  implicit val baseMessage_ModelReader: Reads[BaseMessageModel] = (
    (JsPath \ "message_id").read[Long] and
      (JsPath \ "from").readNullable[UserEntity] and
      (JsPath \ "date").read[Long] and
      (JsPath \ "chat").read[BaseChatModel] and
      (JsPath \ "forward_from").readNullable[UserEntity] and
      (JsPath \ "forward_from_chat").readNullable[BaseChatModel] and
      (JsPath \ "forward_from_message_id").readNullable[Long] and
      (JsPath \ "forward_signature").readNullable[String] and
      (JsPath \ "forward_date").readNullable[Long] and
      (JsPath \ "reply_to_message").readNullable[BaseMessageModel] and
      (JsPath \ "edit_date").readNullable[Long] and
      (JsPath \ "author_signature").readNullable[String] and
      (JsPath \ "text").readNullable[String] and
      (JsPath \ "left_chat_member").readNullable[UserEntity] and
      (JsPath \ "pinned_message").readNullable[BaseMessageModel]
    ) (BaseMessageModel.apply _)

  implicit val getUpdates_ModelReader: Reads[BaseGetUpdatesModel] = (
    (JsPath \ "update_id").read[Long] and
      (JsPath \ "message").readNullable[BaseMessageModel] and
      (JsPath \ "edited_message").readNullable[BaseMessageModel] and
      (JsPath \ "channel_post").readNullable[BaseMessageModel] and
      (JsPath \ "edited_channel_post").readNullable[BaseMessageModel]
    ) (BaseGetUpdatesModel.apply _)

  implicit val getResult_reader: Reads[GetResults] = (
    (JsPath \ "ok").read[Boolean] and
      (JsPath \ "result").read[Seq[BaseGetUpdatesModel]]
    ) (GetResults.apply _)

}
