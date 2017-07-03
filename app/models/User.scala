package models

import play.api.libs.json._

/**
 * ユーザ
 */
case class User(userId: String, userName: String)

object User {
    implicit val userJsonFormat = Json.format[User]
}
