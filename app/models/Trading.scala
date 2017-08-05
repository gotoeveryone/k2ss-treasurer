package models

import javax.inject._
import java.sql.Date
import java.sql.Timestamp
import scala.concurrent._
import play.api.db.slick._
import play.api.libs.json._
import slick.jdbc.JdbcProfile

case class Common(id: Int, create: String, modified: String)

case class Trading(account_id: Int, traded: String, name: Option[String], means: Option[String],
    payment_due_date: Option[String], summary: Option[String], suppliers: Option[String],
    payment: Int, distribution_ratios: Option[Int], create: Timestamp, modified: Timestamp)

class TradingDao @Inject()(dp: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] {
    val dbConfig = dp.get[JdbcProfile]
    import driver.api._

    private val tradings = TableQuery[Tradings]

    class Tradings(tag: Tag) extends Table[Trading] (tag, "tradings") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def account_id = column[Int]("account_id")
        def traded = column[String]("traded")
        def name = column[Option[String]]("name")
        def means = column[Option[String]]("means")
        def payment_due_date = column[Option[String]]("payment_due_date")
        def summary = column[Option[String]]("summary")
        def suppliers = column[Option[String]]("suppliers")
        def payment = column[Int]("payment")
        def distribution_ratios = column[Option[Int]]("distribution_ratios")
        def created = column[Timestamp]("created")
        def modified = column[Timestamp]("modified")

        def * = (account_id, traded, name, means, payment_due_date,
            summary, suppliers, payment, distribution_ratios,
            created, modified) <> (Trading.tupled, Trading.unapply)
    }

    def all(): Future[Seq[Trading]] = dbConfig.db.run(tradings.result)

    def save(trading: Trading) = {
        db.run(tradings.insertOrUpdate(trading))
    }
}
