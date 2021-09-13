package ports.output.repository

import ports.entities.Product
import java.util.UUID

interface ProductRepository {
  @RequiresTransactionContext
  suspend fun findAll(): List<Product>
  @RequiresTransactionContext
  suspend fun findById(id: UUID): Product?
  @RequiresTransactionContext
  suspend fun create(product: Product): Product
  @RequiresTransactionContext
  suspend fun update(id: UUID, product: Product): Product?
  @RequiresTransactionContext
  suspend fun delete(id: UUID)
}
