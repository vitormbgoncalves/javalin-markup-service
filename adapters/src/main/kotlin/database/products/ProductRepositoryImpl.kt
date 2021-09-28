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
  override suspend fun create(product: Product): Product {
    logger.info("Added Product! $product")
    require(product.id == null) { "product.id must be null" }
    return upsertProduct(product = product)
  }

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
  override suspend fun update(product: Product): Product {
    val productId = product.id
    logger.info("Updated Product! $product")
    requireNotNull(productId) { "product.id must not be null" }
    if (!productSQLRepository.hasEntityWithId(id = productId)) {
      throw ProductNotFoundException(searchCriteria = "id=$productId")
    }
    return upsertProduct(product = product)
  }

  @RequiresTransactionContext
  override suspend fun delete(id: UUID) {
    logger.info("Deleted Product! id=$id")
    if (!productSQLRepository.deleteById(id = id)) {
      throw ProductNotFoundException(searchCriteria = "id=$id")
    }
  }

  @RequiresTransactionContext
  private suspend fun upsertProduct(product: Product): Product {
    return when (product.id) {
      null -> Product.fromProductSQLEntity(productSQLEntity = productSQLRepository.upsert(product.toProductSQLEntity()))
      else -> {
        val beforeProduct = findById(product.id!!)
        val afterProduct = Product(
          id = beforeProduct.id,
          name = product.name,
          manufacturer = product.manufacturer,
          money = product.money,
          quantity = product.quantity,
          variable_expenses = product.variable_expenses,
          fixed_expenses = product.fixed_expenses,
          profit_margin = product.profit_margin,
          purchase_date = product.purchase_date,
          created = beforeProduct.created,
          edited = null
        )
        Product.fromProductSQLEntity(productSQLEntity = productSQLRepository.upsert(afterProduct.toProductSQLEntity()))
      }
    }
  }
}
