package adapters.web.routes.products

import adapters.web.routes.products.dto.ProductListResponseDto
import adapters.web.routes.products.dto.ProductRequestDto
import adapters.web.routes.products.dto.ProductResponseDto
import com.reposilite.web.routing.AbstractRoutes
import com.reposilite.web.routing.RouteMethod.DELETE
import com.reposilite.web.routing.RouteMethod.GET
import com.reposilite.web.routing.RouteMethod.POST
import com.reposilite.web.routing.RouteMethod.PUT
import io.javalin.http.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ports.input.ProductUseCases
import java.util.UUID

// Custom context
class AppContext(val context: Context)

//
object ProductRoute : KoinComponent, AbstractRoutes<AppContext, Unit>() {

  private val productUseCases by inject<ProductUseCases>()

  private val addProduct = route("/products", POST) {
    val product = context.bodyAsClass<ProductRequestDto>().toProduct(UUID.randomUUID())
    val insert = productUseCases.addProduct(product)
    context.json(insert)
  }

  private val getProductById = route("/products/:id", GET) {
    val product = productUseCases.getProductById(UUID.fromString(context.pathParam("id")))
    context.json(ProductResponseDto.fromProduct(product!!))
  }

  private val getAllProduct = route("/products", GET) {
    val products = productUseCases.getAllProduct()
    context.json(ProductListResponseDto.fromProductList(products))
  }

  private val updateProduct = route("/products/:id", PUT) {
    val id = UUID.fromString(context.pathParam("id"))
    val product = context.bodyAsClass<ProductRequestDto>().toProduct(id)
    val update = productUseCases.updateProduct(id, product)
    context.json(update!!)
  }

  private val deleteProduct = route("/products/:id", DELETE) {
    val id = UUID.fromString(context.pathParam("id"))
    productUseCases.deleteProduct(id)
  }

  override val routes = setOf(addProduct, getProductById, getAllProduct, updateProduct, deleteProduct)
}
