package core

import entities.Product
import input.ProductUseCases
import output.repository.ProductRepository
import output.repository.RequiresTransactionContext
import output.repository.TransactionService
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
  override suspend fun updateProduct(product: Product): Product? = transactionService.transaction {
    productRepository.update(product)
  }

  @OptIn(RequiresTransactionContext::class)
  override suspend fun deleteProduct(id: UUID) = transactionService.transaction {
    productRepository.delete(id)
  }
}
