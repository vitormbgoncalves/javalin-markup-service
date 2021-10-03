package database.products

import entities.Product
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import output.repository.RequiresTransactionContext
import java.util.UUID

class ProductSQLRepository {

  @RequiresTransactionContext
  fun insert(product: Product): Product {
    return Products
      .insert {
        product.toSqlStatement(it)
      }
      .resultedValues!!
      .first()
      .let {
        Product.fromSqlResultRow(it)
      }
  }

  @RequiresTransactionContext
  fun getAll(): List<Product> {
    return Products
      .selectAll()
      .map {
        Product.fromSqlResultRow(it)
      }
  }

  @RequiresTransactionContext
  fun getByIdOrNull(id: UUID): Product? {
    return Products
      .select {
        Products.id eq id
      }
      .limit(1)
      .map {
        Product.fromSqlResultRow(it)
      }
      .singleOrNull()
  }

  @RequiresTransactionContext
  fun update(product: Product): Product {
    Products
      .update({
        Products.id eq product.id
      }) {
        product.toSqlStatement(it)
      }
    return getByIdOrNull(product.id!!)!!
  }

  @RequiresTransactionContext
  fun deleteById(id: UUID): Boolean {
    return Products
      .deleteWhere {
        Products.id eq id
      } > 0
  }

  @RequiresTransactionContext
  fun count(): Long {
    return Products
      .selectAll()
      .count()
  }

  @RequiresTransactionContext
  fun hasEntityWithId(id: UUID): Boolean {
    return Products
      .select {
        Products.id eq id
      }
      .limit(1)
      .count() > 0
  }
}
