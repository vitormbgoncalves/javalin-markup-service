package adapters.database.products

import adapters.database.utils.pgInsertOrUpdate
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ports.output.repository.RequiresTransactionContext
import java.util.UUID

class ProductSQLRepository {

  private val logger = LogManager.getLogger()

  @RequiresTransactionContext
  fun getAll(): List<ProductSQLEntity> {
    return Products
      .selectAll()
      .map {
        ProductSQLEntity.fromSqlResultRow(it)
      }
  }

  @RequiresTransactionContext
  fun getByIdOrNull(id: UUID): ProductSQLEntity? {
    return Products
      .select {
        Products.id eq id
      }
      .limit(1)
      .map {
        ProductSQLEntity.fromSqlResultRow(it)
      }
      .singleOrNull()
  }

  @RequiresTransactionContext
  fun upsert(entity: ProductSQLEntity): ProductSQLEntity {
    logger.debug("upsert") { "Update/insert $entity for id=${entity.id}" }
    return Products
      .pgInsertOrUpdate(Products.id) {
        entity.toSqlStatement(it)
      }
      .resultedValues!!
      .first()
      .let {
        ProductSQLEntity.fromSqlResultRow(it)
      }
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
