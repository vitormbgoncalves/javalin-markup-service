package database.products

import entities.Product
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.joda.money.CurrencyUnit
import org.joda.money.Money

internal fun Product.toSqlStatement(updateBuilder: UpdateBuilder<Number>) = updateBuilder.let { it ->
  id?.let { id ->
    it[Products.id] = id
  }
  it[Products.name] = name
  it[Products.manufacturer] = manufacturer
  it[Products.cost_price] = cost_price.amount
  it[Products.cost_price_currency] = cost_price.currencyUnit.toString()
  it[Products.markup] = markup
  it[Products.brl_sale_price] = brl_sale_price.amount
  created?.let { created ->
    it[Products.created] = created
  } ?: run { it[Products.created] = LocalDateTime.now() }
  it[Products.edited] = LocalDateTime.now()
}

internal fun Product.Companion.fromSqlResultRow(resultRow: ResultRow) =
  Product(
    id = resultRow[Products.id].value,
    name = resultRow[Products.name],
    manufacturer = resultRow[Products.manufacturer],
    cost_price = Money.of(CurrencyUnit.of(resultRow[Products.cost_price_currency]), resultRow[Products.cost_price]),
    markup = resultRow[Products.markup],
    brl_sale_price = Money.of(CurrencyUnit.of("BRL"), resultRow[Products.brl_sale_price]),
    created = resultRow[Products.created],
    edited = resultRow[Products.edited]
  )
