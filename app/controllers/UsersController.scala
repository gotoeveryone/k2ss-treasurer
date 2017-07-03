package controllers

import javax.inject._
import scala.concurrent._
import scala.concurrent.duration.{ Duration, MILLISECONDS }
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import forms._
import models._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UsersController @Inject()(cc: ControllerComponents, ws: WSClient)
  extends AbstractController(cc) with I18nSupport {

  // ログインフォームを定義
  val loginForm = Form(
    mapping(
      "account" -> text,
      "password" -> text
    )(forms.LoginForm.apply)(forms.LoginForm.unapply)
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
  def login() = Action { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(loginForm)),
      login => {
        val apiUrl = play.Play.application.configuration.getString("api.path")
        val future: Future[WSResponse] = ws
          .url(apiUrl + "web-api/v1/auth/login")
          .withHeaders("Content-Type" -> "application/json")
          .post(Json.obj(
            "account" -> loginForm.bindFromRequest.get.account,
            "password" -> loginForm.bindFromRequest.get.password
          ))

        // 結果からアクセストークンを取得
        val result = Await.result(future, Duration(5000, MILLISECONDS))
        print(result.status)

        if (result.status != 200) {
          Unauthorized(views.html.index(loginForm))
        } else {
          val token: JsResult[String] = (Json.parse(result.body) \ "access_token")
            .validate[String]

          val userFuture: Future[WSResponse] = ws
            .url(apiUrl + "web-api/v1/users")
            .withHeaders("Authorization" -> ("Bearer " + token.get))
            .withHeaders("Content-Type" -> "application/json")
            .get()

          val userResult = Await.result(userFuture, Duration(5000, MILLISECONDS))
          print(userResult.status)
          if (userResult.status != 200) {
            BadRequest(views.html.index(loginForm))
          } else {
            Ok(views.html.menu(Json.parse(userResult.body).as[models.User]))
          }
        }
      }
    )
  }
}
