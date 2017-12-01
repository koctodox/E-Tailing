package services

import javax.inject.Inject

import daos.{BaseChatDao, BaseGetUpdatesDao, BaseMessageDao, UserDao}
import models._
import models.entitys.{BaseChatEntity, BaseGetUpdatesEntity, BaseMessageEntity}

import scala.concurrent.{ExecutionContext, Future}

class GetUpdatesService @Inject()(
                                   userDao: UserDao,
                                   baseChatDao: BaseChatDao,
                                   baseMessageDao: BaseMessageDao,
                                   baseGetUpdatesDao: BaseGetUpdatesDao
                                 )(implicit val executionContext: ExecutionContext) {

  def getUpdates_MOST_DELETED (baseGetUpdates: Seq[BGUT1]): Seq[Future[Int]] = {
    baseGetUpdates map { baseGetUpdate =>
      // TODO => answer this question
      // In this code I think just one of [BaseMessageModel] parameter can be not null (in one item of our sequence). is this true?
      val message_id = baseGetUpdate.message.isEmpty match {
        case true =>
          None
        case false =>
          calculateMessage(baseGetUpdate.message.get)
          Some(baseGetUpdate.message.get.message_id)
      }
      val edited_message_id = baseGetUpdate.edited_message.isEmpty match {
        case true =>
          None
        case false =>
          calculateMessage(baseGetUpdate.edited_message.get)
          Some(baseGetUpdate.edited_message.get.message_id)
      }
      val channel_post_id = baseGetUpdate.channel_post.isEmpty match {
        case true =>
          None
        case false =>
          calculateMessage(baseGetUpdate.channel_post.get)
          Some(baseGetUpdate.channel_post.get.message_id)
      }
      val edited_channel_post_id = baseGetUpdate.edited_channel_post.isEmpty match {
        case true =>
          None
        case false =>
          calculateMessage(baseGetUpdate.edited_message.get)
          Some(baseGetUpdate.edited_channel_post.get.message_id)
      }

      val baseGetUpdatesEntity = BaseGetUpdatesEntity(
        update_id = baseGetUpdate.update_id,
        message_id = message_id,
        edited_message_id = edited_message_id,
        channel_post_id = channel_post_id,
        edited_channel_post_id = edited_channel_post_id
      )

      baseGetUpdatesDao.upsert(baseGetUpdatesEntity)
    }
  }

  private def calculateMessage(baseMessageModel: BMT1): Future[Int] = {

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
        val forwardFrom = baseMessageModel.forward_from.get
        userDao.upsert(forwardFrom)
        Thread.sleep(1000)
        Some(forwardFrom.id)
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
    val left_chat_member_id = baseMessageModel.left_chat_member.isEmpty match {
      case false =>
        userDao.upsert(baseMessageModel.left_chat_member.get)
        Thread.sleep(1000)
        Some(baseMessageModel.left_chat_member.get.id)
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
      edit_date = baseMessageModel.edit_date,
      author_signature = baseMessageModel.author_signature,
      text = baseMessageModel.text,
      left_chat_member_id = left_chat_member_id
    )

    val result = baseMessageDao.upsert(baseMessageEntity)
    Thread.sleep(3000)
    result
  }

  private def calculateLoopMessage(baseLoopMessageModel: BaseLoopMessageModel) = {

    val from_id = baseLoopMessageModel.from.isEmpty match {
      case true =>
        None
      case false =>
        val user = baseLoopMessageModel.from.get
        userDao.upsert(user)
        Thread.sleep(1000)
        Some(user.id)
    }
    val chat_id = {
      calculateChat(baseLoopMessageModel.chat)
      Thread.sleep(1000)
      baseLoopMessageModel.chat.id
    }
    val baseMessageEntity = BaseMessageEntity(
      message_id = baseLoopMessageModel.message_id,
      from_id = from_id,
      date = baseLoopMessageModel.date,
      chat_id = chat_id,
      text = baseLoopMessageModel.text
    )
    val result = baseMessageDao.upsert(baseMessageEntity)
  }

  private def calculateChat(baseChatModel: BaseChatModel): Future[Int] = {
    val baseChatEntity = BaseChatEntity(
      id = baseChatModel.id,
      chat_type = baseChatModel.chat_type,
      title = baseChatModel.title
    )
    baseChatDao.upsert(baseChatEntity)
  }


  //  def getUpdates(baseGetUpdates: Seq[BaseGetUpdatesModel]): Seq[Future[Int]] = {
//    baseGetUpdates map { baseGetUpdate =>
//      // TODO => answer this question
//      // In this code I think just one of [BaseMessageModel] parameter can be not null (in one item of our sequence). is this true?
//      val message_id = baseGetUpdate.message.isEmpty match {
//        case true =>
//          None
//        case false =>
//          calculateMessage(baseGetUpdate.message.get)
//          Some(baseGetUpdate.message.get.message_id)
//      }
//      val edited_message_id = baseGetUpdate.edited_message.isEmpty match {
//        case true =>
//          None
//        case false =>
//          calculateMessage(baseGetUpdate.edited_message.get)
//          Some(baseGetUpdate.edited_message.get.message_id)
//      }
//      val channel_post_id = baseGetUpdate.channel_post.isEmpty match {
//        case true =>
//          None
//        case false =>
//          calculateMessage(baseGetUpdate.channel_post.get)
//          Some(baseGetUpdate.channel_post.get.message_id)
//      }
//      val edited_channel_post_id = baseGetUpdate.edited_channel_post.isEmpty match {
//        case true =>
//          None
//        case false =>
//          calculateMessage(baseGetUpdate.edited_message.get)
//          Some(baseGetUpdate.edited_channel_post.get.message_id)
//      }
//
//      val baseGetUpdatesEntity = BaseGetUpdatesEntity(
//        update_id = baseGetUpdate.update_id,
//        message_id = message_id,
//        edited_message_id = edited_message_id,
//        channel_post_id = channel_post_id,
//        edited_channel_post_id = edited_channel_post_id
//      )
//
//            baseGetUpdatesDao.upsert(baseGetUpdatesEntity)
//    }
//  }
//
//  private def calculateMessage(baseMessageModel: BaseMessageModel): Future[Int] = {
//
//    val from_id = baseMessageModel.from.isEmpty match {
//      case false =>
//        userDao.upsert(baseMessageModel.from.get)
//        Thread.sleep(1000)
//        Some(baseMessageModel.from.get.id)
//      case true =>
//        None
//    }
//    val chat_id = {
//      calculateChat(baseMessageModel.chat)
//      Thread.sleep(1000)
//      baseMessageModel.chat.id
//    }
//    val forward_from_id = baseMessageModel.forward_from.isEmpty match {
//      case false =>
//        val forwardFrom = baseMessageModel.forward_from.get
//        userDao.upsert(forwardFrom)
//        Thread.sleep(1000)
//        Some(forwardFrom.id)
//      case true =>
//        None
//    }
//    val forward_from_chat_id = baseMessageModel.forward_from_chat.isEmpty match {
//      case false =>
//        calculateChat(baseMessageModel.forward_from_chat.get)
//        Thread.sleep(1000)
//        Some(baseMessageModel.forward_from_chat.get.id)
//      case true =>
//        None
//    }
//    val reply_to_message_id = baseMessageModel.reply_to_message.isEmpty match {
//      case false =>
//        calculateLoopMessage(baseMessageModel.reply_to_message.get)
//        Thread.sleep(3000)
//        Some(baseMessageModel.reply_to_message.get.message_id)
//      case true =>
//        None
//    }
//    val left_chat_member_id = baseMessageModel.left_chat_member.isEmpty match {
//      case false =>
//        userDao.upsert(baseMessageModel.left_chat_member.get)
//        Thread.sleep(1000)
//        Some(baseMessageModel.left_chat_member.get.id)
//      case true =>
//        None
//    }
//    val pinned_message_id = baseMessageModel.pinned_message.isEmpty match {
//      case false =>
//        calculateLoopMessage(baseMessageModel.pinned_message.get)
//        Thread.sleep(3000)
//        Some(baseMessageModel.pinned_message.get.message_id)
//      case true =>
//        None
//    }
//
//    val baseMessageEntity = BaseMessageEntity(
//      message_id = baseMessageModel.message_id,
//      from_id = from_id,
//      date = baseMessageModel.date,
//      chat_id = chat_id,
//      forward_from_id = forward_from_id,
//      forward_from_chat_id = forward_from_chat_id,
//      forward_from_message_id = baseMessageModel.forward_from_message_id,
//      forward_signature = baseMessageModel.forward_signature,
//      forward_date = baseMessageModel.forward_date,
//      reply_to_message_id = reply_to_message_id,
//      edit_date = baseMessageModel.edit_date,
//      author_signature = baseMessageModel.author_signature,
//      text = baseMessageModel.text,
//      left_chat_member_id = left_chat_member_id,
//      pinned_message_id = pinned_message_id
//    )
//
//    val result = baseMessageDao.upsert(baseMessageEntity)
//    Thread.sleep(3000)
//    result
//  }
//
//  private def calculateLoopMessage(baseLoopMessageModel: BaseLoopMessageModel) = {
//
//    val from_id = baseLoopMessageModel.from.isEmpty match {
//      case true =>
//        None
//      case false =>
//        val user = baseLoopMessageModel.from.get
//        userDao.upsert(user)
//        Thread.sleep(1000)
//        Some(user.id)
//    }
//    val chat_id = {
//      calculateChat(baseLoopMessageModel.chat)
//      Thread.sleep(1000)
//      baseLoopMessageModel.chat.id
//    }
//    val baseMessageEntity = BaseMessageEntity(
//      message_id = baseLoopMessageModel.message_id,
//      from_id = from_id,
//      date = baseLoopMessageModel.date,
//      chat_id = chat_id,
//      text = baseLoopMessageModel.text
//    )
//    val result = baseMessageDao.upsert(baseMessageEntity)
//  }
//
//  private def calculateChat(baseChatModel: BaseChatModel): Future[Int] = {
//    val baseChatEntity = BaseChatEntity(
//      id = baseChatModel.id,
//      chat_type = baseChatModel.chat_type,
//      title = baseChatModel.title
//    )
//    baseChatDao.upsert(baseChatEntity)
//  }

}
