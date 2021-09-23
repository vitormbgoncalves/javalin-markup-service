package core

import org.koin.dsl.binds
import org.koin.dsl.module
import ports.input.ProductUseCases

// Core module for Dependency Injection
val coreModule = module(createdAtStart = true) {

  // Services for use cases

  single {
    ProductService(
      transactionService = get(),
      productRepository = get()
    )
  } binds arrayOf(
    ProductUseCases::class
  )
}
