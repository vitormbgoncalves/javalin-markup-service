package adapters.database.products

import org.apache.logging.log4j.LogManager
import ports.entities.Product
import ports.output.repository.ProductNotFoundException
import ports.output.repository.ProductRepository
import ports.output.repository.RequiresTransactionContext
import java.util.UUID

class ProductRepositoryImpl(
  private val productSQLRepository: ProductSQLRepository
) : ProductRepository {

  private val logger = LogManager.getLogger()

  @RequiresTransactionContext
  override suspend fun findAll(): List<Product> {
    val products = productSQLRepository.getAll()
    return products.map { Product.fromProductSQLEntity(it) }
  }

  @RequiresTransactionContext
  override suspend fun findById(id: UUID): Product {
    val product = productSQLRepository.getByIdOrNull(id = id)
      ?: throw ProductNotFoundException(searchCriteria = "id=$id")
    return Product.fromProductSQLEntity(productSQLEntity = product)
  }

  @RequiresTransactionContext
  override suspend fun create(product: Product): Product {
    logger.debug("addProduct") { "Add Product: $product" }
    require(product.id == null) { "product.id must be null" }
    return upsertProduct(product = product)
  }

  @RequiresTransactionContext
  override suspend fun update(id: UUID, product: Product): Product {
    val productId = product.id
    logger.debug("updateProduct") { "Update Product by id=$productId: $product" }
    requireNotNull(productId) { "product.id must not be null" }
    if (!productSQLRepository.hasEntityWithId(id = productId)) {
      throw ProductNotFoundException(searchCriteria = "id=$productId")
    }
    return upsertProduct(product = product)
  }

  @RequiresTransactionContext
  override suspend fun delete(id: UUID) {
    logger.debug("deleteProduct") { "Delete product by id=$id" }
    if (!productSQLRepository.deleteById(id = id)) {
      throw ProductNotFoundException(searchCriteria = "id=$id")
    }
  }

  @RequiresTransactionContext
  private fun upsertProduct(product: Product): Product {
    val productInsert = productSQLRepository.upsert(product.toProductSQLEntity())
    return Product.fromProductSQLEntity(productSQLEntity = productInsert)
  }
}
