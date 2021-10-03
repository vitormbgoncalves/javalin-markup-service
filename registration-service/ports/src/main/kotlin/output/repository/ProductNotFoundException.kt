package ports.output.repository

import ports.output.errors.ResourceNotFoundException

class ProductNotFoundException(
  val searchCriteria: String
) : ResourceNotFoundException(
  title = "Product not found",
  detail = "Product not found for search criteria: $searchCriteria"
)
