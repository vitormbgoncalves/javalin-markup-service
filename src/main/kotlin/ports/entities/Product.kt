package ports.entities

import org.joda.money.Money
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Product(
  val id: UUID? = null,
  val name: String,
  val manufacturer: String,
  val money: Money,
  val amount: Long,
  val variable_expenses: BigDecimal,
  val fixed_expenses: BigDecimal,
  val profit_margin: Double,
  val purchase_date: LocalDate,
  val created: LocalDateTime,
  val edited: LocalDateTime
) {
  companion object
}
