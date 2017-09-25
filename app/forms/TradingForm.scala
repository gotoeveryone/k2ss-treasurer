package forms

/**
 * 取引フォーム
 */
case class TradingForm(
    accountId: Int,
    traded: String,
    name: Option[String],
    means: Option[String],
    paymentDueDate: Option[String],
    summary: Option[String],
    suppliers: Option[String],
    payment: Int,
    distributionRatios: Option[Int]
)
