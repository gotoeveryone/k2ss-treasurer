package controllers

import javax.inject._
import scala.concurrent._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.json._
import play.api.db.slick._
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver
import forms._
import models._
import ExecutionContext.Implicits.global

/**
 * ログイン用コントローラ
 */
@Singleton
class UsersController @Inject()(conf: Configuration, cc: ControllerComponents,
  ws: WSClient, dao: UserDao, messagesApi: MessagesApi) extends AbstractController(cc) with I18nSupport {

  // ログインフォームを定義
  val loginForm = Form(
    mapping(
      "account" -> nonEmptyText(6, 10),
      "password" -> nonEmptyText(8, 20)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  /**
   * 初期処理
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(loginForm))
  }

  /**
   * ログイン処理
   */
  def login() = Action.async { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      errors => Future(BadRequest(views.html.index(loginForm.bindFromRequest))),
      login => {
        val apiUrl = conf.get[String]("api.path")

        val params = Json.obj(
          "account" -> loginForm.bindFromRequest.get.account,
          "password" -> loginForm.bindFromRequest.get.password
        )

        // 認証エラーメッセージ
        val flash = Flash(Map("error" -> "aaaaa"))

        // 認証
        ws.url(apiUrl + "web-api/v1/auth/login")
          .addHttpHeaders("Content-Type" -> "application/json")
          .post(params).flatMap {result =>
            result.status match {
              case 200 => {
                val token: JsResult[String] = (Json.parse(result.body) \ "access_token")
                  .validate[String]

                // トークンを利用してユーザ情報を取得
                ws.url(apiUrl + "web-api/v1/users")
                  .addHttpHeaders("Content-Type" -> "application/json")
                  .addHttpHeaders("Authorization" -> ("Bearer " + token.get))
                  .get().flatMap {result =>
                    result.status match {
                      case 200 => {
                          // ユーザオブジェクト生成
                          val user = Json.parse(result.body).as[User]
                          Future(Ok(views.html.menu(user))
                            .withSession("user" -> user.userName))
                      }
                      case _ => Future(Unauthorized(views.html.index(loginForm)))
                    }
                  }
              }
              case _ => Future(Unauthorized(views.html.index(loginForm)))
            }
          }
      }
    )
  }
}
