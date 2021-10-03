package entities

import org.joda.money.Money
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Product(
  val id: UUID? = null,
  val name: String,
  val manufacturer: String,
  val cost_price: Money,
  val markup: BigDecimal,
  val brl_sale_price: Money,
  val created: LocalDateTime? = null,
  val edited: LocalDateTime? = null
) {
  companion object
}
