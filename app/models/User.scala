package models

import javax.inject._
import scala.concurrent._
import play.api.db.slick._
import play.api.libs.json._
import slick.jdbc.JdbcProfile


/**
 * ユーザ
 */
case class User(id: Int, userId: String, userName: String)

object User {
    implicit val userFormat = Json.format[User]
}

class UserDao @Inject()(dp: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] {
    val dbConfig = dp.get[JdbcProfile]
    import driver.api._

    private val users = TableQuery[UsersTable]

    class UsersTable(tag: Tag) extends Table[User] (tag, "users") {
        def id = column[Int]("id", O.PrimaryKey)
        def account = column[String]("account")
        def name = column[String]("name")

        def * = (id, account, name) <> ((User.apply _).tupled, User.unapply)
    }

    def all(): Future[Seq[User]] = dbConfig.db.run(users/*.filter(_.active === true)*/.result)
}
