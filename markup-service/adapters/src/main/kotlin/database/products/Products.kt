package database.products

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

// --- TABLE PRODUCTS
internal object Products : UUIDTable() {
  val name = text("name")
  val manufacturer = text("manufacturer")
  val cost_price = decimal("cost_price", 10, 2)
  val cost_price_currency = reference("cost_price_currency", Currency.name)
  val markup = decimal("markup", 10, 2)
  val brl_sale_price = decimal("brl_sale_price", 10, 2)
  val created = datetime("created_date")
  val edited = datetime("edited_date")
}

// --- TABLE CURRENCY
internal object Currency : IntIdTable() {
  val name = char("name", 3).uniqueIndex()
}
