package services

import javax.inject.Inject

import daos.{BaseChatDao, BaseGetUpdatesDao, BaseMessageDao, UserDao}
import models.{BaseChatModel, BaseGetUpdatesModel, BaseMessageModel}
import models.entitys.{BaseChatEntity, BaseGetUpdatesEntity, BaseMessageEntity}

import scala.concurrent.{ExecutionContext, Future}

class GetUpdatesService @Inject()(
                                   userDao: UserDao,
                                   baseChatDao: BaseChatDao,
                                   baseMessageDao: BaseMessageDao,
                                   baseGetUpdatesDao: BaseGetUpdatesDao
                                 )(implicit val executionContext: ExecutionContext) {

  def getUpdates(baseGetUpdates: Seq[BaseGetUpdatesModel]): Seq[Future[Int]] = {
    baseGetUpdates map { baseGetUpdate =>
      // TODO => answer this question
      // In this code I think just one of [BaseMessageModel] parameter can be not null (in one item of our sequence). is this true?

      val baseGetUpdatesEntity = baseGetUpdate.message.isEmpty match {
        case false =>
          calculateMessage(baseGetUpdate.message.get)
          BaseGetUpdatesEntity(
            update_id = baseGetUpdate.update_id,
            message_id = Some(baseGetUpdate.message.get.message_id)
          )
        case true =>
          baseGetUpdate.edited_message.isEmpty match {
            case false =>
              calculateMessage(baseGetUpdate.edited_message.get)
              BaseGetUpdatesEntity(
                update_id = baseGetUpdate.update_id,
                edited_message_id = Some(baseGetUpdate.edited_message.get.message_id)
              )
            case true =>
              baseGetUpdate.channel_post.isEmpty match {
                case false =>
                  calculateMessage(baseGetUpdate.channel_post.get)
                  BaseGetUpdatesEntity(
                    update_id = baseGetUpdate.update_id,
                    channel_post_id = Some(baseGetUpdate.channel_post.get.message_id)
                  )
                case true =>
                  baseGetUpdate.edited_channel_post.isEmpty match {
                    case false =>
                      calculateMessage(baseGetUpdate.edited_message.get)
                      BaseGetUpdatesEntity(
                        update_id = baseGetUpdate.update_id,
                        edited_channel_post_id = Some(baseGetUpdate.edited_channel_post.get.message_id)
                      )
                    case true =>
                      BaseGetUpdatesEntity(
                        update_id = baseGetUpdate.update_id
                      )
                  }
              }
          }
      }

      baseGetUpdatesDao.upsert(baseGetUpdatesEntity)
    }
  }

  private def calculateMessage(baseMessageModel: BaseMessageModel): Future[Int] = {

    val from_id = baseMessageModel.from.isEmpty match {
        case false =>
          userDao.upsert(baseMessageModel.from.get)
          Thread.sleep(1000)
          Some(baseMessageModel.from.get.id)
        case true =>
          None
      }
    val chat_id = {
      calculateChat(baseMessageModel.chat)
      Thread.sleep(1000)
      baseMessageModel.chat.id
    }
    val forward_from_id = baseMessageModel.forward_from.isEmpty match {
        case false =>
          userDao.upsert(baseMessageModel.forward_from.get)
          Thread.sleep(1000)
          Some(baseMessageModel.forward_from.get.id)
        case true =>
          None
      }
    val forward_from_chat_id = baseMessageModel.forward_from_chat.isEmpty match {
      case false =>
        calculateChat(baseMessageModel.forward_from_chat.get)
        Thread.sleep(1000)
        Some(baseMessageModel.forward_from_chat.get.id)
      case true =>
        None
    }
    val reply_to_message_id = baseMessageModel.reply_to_message.isEmpty match {
      case false =>
        calculateMessage(baseMessageModel.reply_to_message.get)
        Thread.sleep(3000)
        Some(baseMessageModel.reply_to_message.get.message_id)
      case true =>
        None
    }
    val left_chat_member_id = baseMessageModel.left_chat_member.isEmpty match {
      case false =>
        userDao.upsert(baseMessageModel.left_chat_member.get)
        Thread.sleep(1000)
        Some(baseMessageModel.left_chat_member.get.id)
      case true =>
        None
    }
    val pinned_message_id = baseMessageModel.pinned_message.isEmpty match {
      case false =>
        calculateMessage(baseMessageModel.pinned_message.get)
        Thread.sleep(3000)
        Some(baseMessageModel.pinned_message.get.message_id)
      case true =>
        None
    }

    val baseMessageEntity = BaseMessageEntity(
      message_id = baseMessageModel.message_id,
      from_id = from_id,
      date = baseMessageModel.date,
      chat_id = chat_id,
      forward_from_id = forward_from_id,
      forward_from_chat_id = forward_from_chat_id,
      forward_from_message_id = baseMessageModel.forward_from_message_id,
      forward_signature = baseMessageModel.forward_signature,
      forward_date = baseMessageModel.forward_date,
      reply_to_message_id = reply_to_message_id,
      edit_date = baseMessageModel.edit_date,
      author_signature = baseMessageModel.author_signature,
      text = baseMessageModel.text,
      left_chat_member_id = left_chat_member_id,
      pinned_message_id = pinned_message_id
    )

    val result = baseMessageDao.upsert(baseMessageEntity)

    Thread.sleep(3000)

    result
  }

  private def calculateChat(baseChatModel: BaseChatModel): Future[Int] ={
    val baseChatEntity = BaseChatEntity(
      id = baseChatModel.id,
      chat_type = baseChatModel.chat_type,
      title = baseChatModel.title
    )
    baseChatDao.upsert(baseChatEntity)
  }

}
