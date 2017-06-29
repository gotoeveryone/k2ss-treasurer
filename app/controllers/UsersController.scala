package controllers

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._

/**
 * ログインフォーム
 */
case class LoginForm(account: String, password: String)

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UsersController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  // ログインフォームを定義
  val loginForm = Form(
    mapping(
      "account" -> text,
      "password" -> text
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
  def login() = Action { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(loginForm)),
      login => {
        Ok(views.html.menu())
      }
    )
  }
}
