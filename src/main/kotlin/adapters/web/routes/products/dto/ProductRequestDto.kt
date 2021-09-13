package adapters.web.routes.products.dto

import org.joda.money.Money
import ports.entities.Product
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class ProductRequestDto(
  val name: String,
  val manufacturer: String,
  val money: Money,
  val amount: Long,
  val variable_expenses: BigDecimal,
  val fixed_expenses: BigDecimal,
  val profit_margin: Double,
  val purchase_date: LocalDate,
) {

  fun toProduct(id: UUID?) = Product(
    id = id,
    name = name,
    manufacturer = manufacturer,
    money = money,
    amount = amount,
    variable_expenses = variable_expenses,
    fixed_expenses = fixed_expenses,
    profit_margin = profit_margin,
    purchase_date = purchase_date,
    created = LocalDateTime.now(),
    edited = LocalDateTime.now()
  )
}
