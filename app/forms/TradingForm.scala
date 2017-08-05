package forms

/**
 * 取引フォーム
 */
case class TradingForm(account_id: Int, traded: String, name: Option[String], means: Option[String],
    payment_due_date: Option[String], summary: Option[String], suppliers: Option[String],
    payment: Int, distribution_ratios: Option[Int])
