package database.products

import entities.Product
import org.apache.logging.log4j.LogManager
import output.repository.ProductNotFoundException
import output.repository.ProductRepository
import output.repository.RequiresTransactionContext
import java.util.UUID

class ProductRepositoryImpl(
  private val productSQLRepository: ProductSQLRepository
) : ProductRepository {

  private val logger = LogManager.getLogger()

  @RequiresTransactionContext
  override suspend fun create(product: Product): Product {
    logger.info("Added Product! $product")
    require(product.id == null) { "product.id must be null" }
    return productSQLRepository.insert(product)
  }

  @RequiresTransactionContext
  override suspend fun findAll(): List<Product> {
    return productSQLRepository.getAll()
  }

  @RequiresTransactionContext
  override suspend fun findById(id: UUID): Product {
    return productSQLRepository.getByIdOrNull(id) ?: throw ProductNotFoundException(searchCriteria = "id=$id")
  }

  @RequiresTransactionContext
  override suspend fun update(product: Product): Product {
    val productId = product.id
    logger.info("Updated Product! $product")
    requireNotNull(productId) { "product.id must not be null" }
    if (!productSQLRepository.hasEntityWithId(id = productId)) {
      throw ProductNotFoundException(searchCriteria = "id=$productId")
    }
    return productSQLRepository.update(product)
  }

  @RequiresTransactionContext
  override suspend fun delete(id: UUID) {
    logger.info("Deleted Product! id=$id")
    if (!productSQLRepository.deleteById(id = id)) {
      throw ProductNotFoundException(searchCriteria = "id=$id")
    }
  }
}
