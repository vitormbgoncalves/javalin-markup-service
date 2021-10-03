package core

import input.ProductUseCases
import org.koin.dsl.module

val coreModule = module(createdAtStart = true) {

  single<ProductUseCases> {
    ProductService(
      transactionService = get(),
      productRepository = get()
    )
  }
}
