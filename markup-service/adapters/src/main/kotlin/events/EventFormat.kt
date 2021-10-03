package adapters.events

import entities.Product
import org.joda.money.Money
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class EventFormat(
  val eventType: String,
  val data: SubscribeEventDTO,
  val timestamp: Timestamp
) {
  companion object
}

data class SubscribeEventDTO(
  val id: UUID,
  val name: String? = null,
  val manufacturer: String? = null,
  val money: Money? = null,
  val quantity: Long? = null,
  val variable_expenses: BigDecimal? = null,
  val fixed_expenses: BigDecimal? = null,
  val profit_margin: Double? = null,
  val purchase_date: LocalDate? = null,
  val created: LocalDateTime? = null,
  val edited: LocalDateTime? = null
)

internal fun Product.Companion.fromBrokerEvent(event: EventFormat) =
  Product(
    id = null,
    name = event.data.name!!,
    manufacturer = event.data.manufacturer!!,
    cost_price = event.data.money!!,
    markup = BigDecimal.TEN,
    brl_sale_price = event.data.money,
    created = null,
    edited = null
  )
