package controllers

import javax.inject._
import scala.concurrent._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.json._
import play.api.db.slick._
import ExecutionContext.Implicits.global
import forms._
import models._

/**
 * 取引用コントローラ
 */
@Singleton
class TradingsController @Inject()(conf: Configuration, cc: ControllerComponents,
    ws: WSClient, dao: TradingDao, messagesApi: MessagesApi) extends AbstractController(cc) with I18nSupport {

    val form = Form(
        mapping(
            "accountId" -> number(0),
            "traded" -> nonEmptyText(10),
            "name" -> optional(text(0, 20)),
            "means" -> optional(text(0, 20)),
            "paymentDueDate" -> optional(text(0, 20)),
            "summary" -> optional(text(0, 20)),
            "suppliers" -> optional(text(0, 20)),
            "payment" -> number(0, 9999999),
            "distributionRatios" -> optional(number(0, 1))
        )(TradingForm.apply)(TradingForm.unapply)
    )

    /**
     * 初期処理
     */
    def index() = Action.async { implicit request: Request[AnyContent] =>
        dao.all().map(results => Ok(views.html.tradings(form, results)))
    }

    /**
     * 初期処理
     */
    def search() = Action.async { implicit request: Request[AnyContent] =>
        form.bindFromRequest.fold(
            error => dao.all().map(results =>
                BadRequest(views.html.tradings(form.bindFromRequest, results))),
            success => {
                val input = form.bindFromRequest().get
                dao.filter(input).map(results => Ok(views.html.tradings(form, results)))
            }
        )
    }

    /**
     * 登録処理
     */
    def add() = Action.async { implicit request =>
        form.bindFromRequest.fold(
            error => dao.all().map(results =>
                BadRequest(views.html.tradings(form.bindFromRequest, results))),
            success => {
                val input = form.bindFromRequest().get
                dao.save(input).map(io => io.toInt).map(id => Logger.info(s"追加しました。ID=$id"))
                dao.all().map(results => Ok(views.html.tradings(form, results)))
            }
        )
    }
}
