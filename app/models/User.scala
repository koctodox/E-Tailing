package models

case class User(
                 id: Long,
                 is_bot: Boolean = false,
                 first_name: String,
                 last_name: Option[String] = None,
                 username: Option[String] = None,
                 language_code: Option[String] = None
               )
//More helps in:
//https://core.telegram.org/bots/api#user
