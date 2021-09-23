package adapters.database.products

import adapters.database.products.Products.quantity
import ports.entities.Product

internal fun Product.Companion.fromProductSQLEntity(
  productSQLEntity: ProductSQLEntity
) = with(productSQLEntity) {
  Product(
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

internal fun Product.toProductSQLEntity() = with(this) {
  ProductSQLEntity(
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
