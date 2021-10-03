package output.repository

import output.errors.ResourceNotFoundException

class ProductNotFoundException(
  private val searchCriteria: String
) : ResourceNotFoundException(
  title = "Product not found",
  detail = "Product not found for search criteria: $searchCriteria"
)
