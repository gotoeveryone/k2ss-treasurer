package controllers

import javax.inject._
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDateTime
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
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver
import forms._
import models._
import ExecutionContext.Implicits.global

/**
 * 取引用コントローラ
 */
@Singleton
class TradingsController @Inject()(conf: Configuration, cc: ControllerComponents,
    ws: WSClient, dao: TradingDao, messagesApi: MessagesApi) extends AbstractController(cc) with I18nSupport {

    val form = Form(
        mapping(
            "account_id" -> number(0),
            "traded" -> nonEmptyText(10),
            "name" -> optional(text(0, 20)),
            "means" -> optional(text(0, 20)),
            "payment_due_date" -> optional(text(0, 20)),
            "summary" -> optional(text(0, 20)),
            "suppliers" -> optional(text(0, 20)),
            "payment" -> number(0, 9999999),
            "distribution_ratios" -> optional(number(0, 1))
        )(TradingForm.apply)(TradingForm.unapply)
    )

    /**
     * 初期処理
     */
    def index() = Action.async { implicit request: Request[AnyContent] =>
        dao.all().map(results => Ok(views.html.tradings(form, results)))
    }

    /**
     * 登録処理
     */
    def add() = Action.async { implicit request =>
        form.bindFromRequest.fold(
            error => Future(Redirect(routes.TradingsController.index, 400)),
            success => {
                val input = form.bindFromRequest().get
                val now = Timestamp.valueOf(LocalDateTime.now())
                val newRec = Trading(input.account_id, input.traded,
                    input.name, input.means, input.payment_due_date, input.summary,
                    input.suppliers, input.payment, input.distribution_ratios, now, now)
                val id = dao.save(newRec).map(io => io.toInt)
        dao.all().map(results => Ok(views.html.tradings(form, results)))
            }
        )
    }
}
