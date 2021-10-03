package web.routes.products.dto

import entities.Product
import org.joda.money.Money
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class ProductResponseDto(
  val id: UUID,
  val name: String,
  val manufacturer: String,
  val cost_price: Money,
  val markup: BigDecimal,
  val brl_sale_price: Money,
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
          cost_price = cost_price,
          markup = markup,
          brl_sale_price = brl_sale_price,
          created = created!!,
          edited = edited!!
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
