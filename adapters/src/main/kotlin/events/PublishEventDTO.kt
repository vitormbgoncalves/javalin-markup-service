package adapters.events

import adapters.web.routes.products.dto.ProductResponseDto
import org.joda.money.Money
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class PublishEventDTO(
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
) {
  companion object {
    fun fromProductResponseDto(product: ProductResponseDto): PublishEventDTO {
      return with(product) {
        PublishEventDTO(
          id = id,
          name = name,
          manufacturer = manufacturer,
          money = money,
          quantity = quantity,
          variable_expenses = variable_expenses,
          fixed_expenses = fixed_expenses,
          profit_margin = profit_margin,
          purchase_date = purchase_date,
          created = created,
          edited = edited
        )
      }
    }
  }
}
