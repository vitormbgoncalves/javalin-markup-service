package input

import entities.Product
import java.util.UUID

interface ProductUseCases {
  suspend fun addProduct(product: Product): Product
  suspend fun getAllProduct(): List<Product>
  suspend fun getProductById(id: UUID): Product?
  suspend fun updateProduct(product: Product): Product?
  suspend fun deleteProduct(id: UUID)
}
