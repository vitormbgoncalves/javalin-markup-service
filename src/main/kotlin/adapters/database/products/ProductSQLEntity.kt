package adapters.database.products

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.money.compositeMoney
import org.joda.money.Money
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

// --- TABLE
internal object Products : Table("products") {
  val id = uuid("id").autoIncrement()
  val name = text("name").index()
  val manufacturer = text("manufacturer")
  val money = compositeMoney(8, 5, "money")
  val amount = long("amount")
  val variable_expenses = decimal("variable_expenses", 19, 4)
  val fixed_expenses = decimal("fixed_expenses", 19, 4)
  val profit_margin = double("profit_margin")
  val purchase_date = date("purchase_date")
  val created = datetime("created_date")
  val edited = datetime("edited_date")

  override val primaryKey = PrimaryKey(id, name = "PK_product_id")

  init {
    index(isUnique = true, columns = arrayOf(name))
  }
}

// --- ENTITY
data class ProductSQLEntity(
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
