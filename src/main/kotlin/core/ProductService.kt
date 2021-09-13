package core

import ports.entities.Product
import ports.input.ProductUseCases
import ports.output.repository.ProductRepository
import ports.output.repository.RequiresTransactionContext
import ports.output.repository.TransactionService
import java.util.UUID

class ProductService(private val transactionService: TransactionService, private val productRepository: ProductRepository) : ProductUseCases {
  @OptIn(RequiresTransactionContext::class)
  override suspend fun addProduct(product: Product): Product = transactionService.transaction {
    productRepository.create(product)
  }

  @OptIn(RequiresTransactionContext::class)
  override suspend fun getAllProduct(): List<Product> = transactionService.transaction {
    productRepository.findAll()
  }

  @OptIn(RequiresTransactionContext::class)
  override suspend fun getProductById(id: UUID): Product? = transactionService.transaction {
    productRepository.findById(id)
  }

  @OptIn(RequiresTransactionContext::class)
  override suspend fun updateProduct(id: UUID, product: Product): Product? = transactionService.transaction {
    productRepository.update(id, product)
  }

  @OptIn(RequiresTransactionContext::class)
  override suspend fun deleteProduct(id: UUID) = transactionService.transaction {
    productRepository.delete(id)
  }
}
