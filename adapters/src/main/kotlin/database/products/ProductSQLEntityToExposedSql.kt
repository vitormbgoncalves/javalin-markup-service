package adapters.database.products

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.time.LocalDateTime

internal fun ProductSQLEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
  id?.let { id ->
    it[Products.id] = id
  }
  it[Products.name] = name
  it[Products.manufacturer] = manufacturer
  it[Products.price] = money.amount
  it[Products.currency] = money.currencyUnit.toString()
  it[Products.quantity] = quantity
  it[Products.variable_expenses] = variable_expenses
  it[Products.fixed_expenses] = fixed_expenses
  it[Products.profit_margin] = profit_margin
  it[Products.purchase_date] = purchase_date
  created?.let { created ->
    it[Products.created] = created
  } ?: run { LocalDateTime.now() }
  it[Products.edited] = LocalDateTime.now()
}

internal fun ProductSQLEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
  ProductSQLEntity(
    id = resultRow[Products.id].value,
    name = resultRow[Products.name],
    manufacturer = resultRow[Products.manufacturer],
    money = Money.of(CurrencyUnit.of(resultRow[Products.currency]), resultRow[Products.price]),
    quantity = resultRow[Products.quantity],
    variable_expenses = resultRow[Products.variable_expenses],
    fixed_expenses = resultRow[Products.fixed_expenses],
    profit_margin = resultRow[Products.profit_margin],
    purchase_date = resultRow[Products.purchase_date],
    created = resultRow[Products.created],
    edited = resultRow[Products.edited]
  )
