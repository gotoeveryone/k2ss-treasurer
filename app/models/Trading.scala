package models

import javax.inject._
import java.sql._
import java.time._
import java.time.format._
import scala.concurrent._
import play.api.db.slick._
import play.api.libs.json._
import slick.jdbc.JdbcProfile
import forms._

/**
 * 基底
 */
sealed trait Keys {
    def id: Int
    def created: LocalDateTime
    def modified: LocalDateTime
}

/**
 * 取引
 */
case class Trading(
    override val id: Int,
    override val created: LocalDateTime,
    override val modified: LocalDateTime,
    accountId: Int,
    traded: LocalDate,
    name: Option[String],
    means: Option[String],
    paymentDueDate: Option[String],
    summary: Option[String],
    suppliers: Option[String],
    payment: Int,
    distributionRatios: Option[Int],
) extends Keys

object Trading {
    implicit val userFormat = Json.format[Trading]
}

/**
 * 取引DAO
 */
class TradingDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[JdbcProfile] {

    import profile.api._

    /**
     * TimestampをLocalDateTimeとして使う
     */
    implicit val datetimeColumnType = MappedColumnType.base[LocalDateTime, Timestamp](
        { l => Timestamp.valueOf(l) },
        { t => t.toLocalDateTime() }
    )

    /**
     * TimestampをLocalDateとして使う
     */
    implicit val dateColumnType = MappedColumnType.base[LocalDate, Timestamp](
        { l => Timestamp.from(l.atStartOfDay(ZoneId.systemDefault()).toInstant()) },
        { t => t.toLocalDateTime().toLocalDate() }
    )

    private val items = TableQuery[Tradings]

    class Tradings(tag: Tag) extends Table[Trading] (tag, "tradings") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def account_id = column[Int]("account_id")
        def traded = column[LocalDate]("traded")
        def name = column[Option[String]]("name")
        def means = column[Option[String]]("means")
        def payment_due_date = column[Option[String]]("payment_due_date")
        def summary = column[Option[String]]("summary")
        def suppliers = column[Option[String]]("suppliers")
        def payment = column[Int]("payment")
        def distribution_ratios = column[Option[Int]]("distribution_ratios")
        def created = column[LocalDateTime]("created")
        def modified = column[LocalDateTime]("modified")

        def * = (id, created, modified, account_id, traded, name, means, payment_due_date,
            summary, suppliers, payment, distribution_ratios) <> ((Trading.apply _).tupled, Trading.unapply)
    }

    /**
     * 全件検索
     */
    def all(): Future[Seq[Trading]] = db.run(items.result)

    /**
     * フィルタ
     */
    def filter(form: TradingForm): Future[Seq[Trading]] = {
        val filter = (x: Tradings) => List(
            if (!form.name.isEmpty) Some(x.name === form.name.get) else None,
            if (!form.means.isEmpty) Some(x.name === form.means.get) else None
        ).collect({
            case Some(criteria) => criteria
        }).reduceLeft(_ && _)
        db.run(items.filter(filter).result)
    }

    /**
     * 保存
     */
    def save(form: TradingForm) = {
        val now = LocalDateTime.now()
        val trading = Trading(0, now, now, form.accountId,
            LocalDate.parse(form.traded, DateTimeFormatter.ofPattern("yyyy/MM/dd")),
            form.name, form.means, form.paymentDueDate, form.summary,
            form.suppliers, form.payment, form.distributionRatios)
        db.run(items.insertOrUpdate(trading))
    }
}
