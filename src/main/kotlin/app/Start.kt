package app

import adapters.web.routes.products.ProductRoute.getAllProduct
import adapters.web.routes.products.ProductRoute.getProductById
import io.javalin.Javalin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun main(): Unit = withContext(Dispatchers.IO) {

  val app = Javalin.create().start(8080)

  app.get("/") { ctx -> ctx.result("Hello World") }

  app.get("/products") { ctx ->
    launch { getProductById(ctx) }
  }

  app.get("/products:id") { ctx ->
    launch { getAllProduct(ctx) }
  }
}
