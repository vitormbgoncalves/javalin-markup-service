package adapters.database.products

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.joda.money.Money
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

// --- TABLE
internal object Products : UUIDTable("products") {
  val name = text("name")
  val manufacturer = text("manufacturer")
  val price = decimal("price", 10, 2)
  val currency = text("currency").index()
  val quantity = long("quantity")
  val variable_expenses = decimal("variable_expenses", 19, 4)
  val fixed_expenses = decimal("fixed_expenses", 19, 4)
  val profit_margin = double("profit_margin")
  val purchase_date = date("purchase_date")
  val created = datetime("created_date")
  val edited = datetime("edited_date")

  /*init {
    index(isUnique = true, columns = arrayOf(currency))
  }*/
}

// --- ENTITY
data class ProductSQLEntity(
  val id: UUID? = null,
  val name: String,
  val manufacturer: String,
  val money: Money,
  val quantity: Long,
  val variable_expenses: BigDecimal,
  val fixed_expenses: BigDecimal,
  val profit_margin: Double,
  val purchase_date: LocalDate,
  val created: LocalDateTime? = null,
  val edited: LocalDateTime? = null
) {
  companion object
}
