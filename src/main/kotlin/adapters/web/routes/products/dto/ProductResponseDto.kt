package adapters.web.routes.products.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.joda.money.Money
import ports.entities.Product

data class ProductResponseDto(
  val id: UUID,
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
  companion object {
    fun fromProduct(product: Product): ProductResponseDto {
      requireNotNull(product.id) { "product.id must not be null" }
      return with(product) {
        ProductResponseDto(
          id = id!!,
          name = name,
          manufacturer = manufacturer,
          money = money,
          amount = amount,
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

data class ProductListResponseDto(
  val index: Int,
  val count: Int,
  val total: Int,
  val items: List<ProductResponseDto>
) {
  companion object {
    fun fromProductList(
      products: List<Product>
    ): ProductListResponseDto {
      val items = products.map {
        ProductResponseDto.fromProduct(it)
      }
      return ProductListResponseDto(
        items = items,
        index = 0,
        count = items.size,
        total = items.size
      )
    }
  }
}
