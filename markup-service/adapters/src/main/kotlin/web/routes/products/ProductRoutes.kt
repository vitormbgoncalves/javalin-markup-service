package web.routes.products

import adapters.events.kafka.Consumer
import com.reposilite.web.http.ErrorResponse
import com.reposilite.web.http.error
import com.reposilite.web.routing.RouteMethod
import input.ProductUseCases
import io.javalin.openapi.HttpMethod
import io.javalin.openapi.OpenApi
import io.javalin.openapi.OpenApiContent
import io.javalin.openapi.OpenApiParam
import io.javalin.openapi.OpenApiResponse
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import output.repository.ProductNotFoundException
import web.config.AsyncRoute
import web.config.AsyncRoutes
import web.routes.products.dto.ProductListResponseDto
import web.routes.products.dto.ProductListResponseDto.Companion.fromProductList
import web.routes.products.dto.ProductResponseDto
import web.routes.products.dto.ProductResponseDto.Companion.fromProduct
import java.util.UUID

internal class ProductRoutes : KoinComponent, AsyncRoutes() {

  private val productUseCases by inject<ProductUseCases>()
  private val logger = LogManager.getLogger()

  init {
    Consumer.message(productUseCases).start("markup-service", maxParallelHint = 2)
  }

  @OpenApi(
    path = "/products",
    methods = [HttpMethod.GET],
    summary = "Get All Products",
    description = "Return all products",
    tags = [ "Product" ],
    responses = [
      OpenApiResponse(
        status = "200",
        description = "Products found!",
        content = [ OpenApiContent(from = ProductListResponseDto::class) ]
      ),
      OpenApiResponse(
        status = "404",
        description = "Not found",
        content = [ OpenApiContent(from = ErrorResponse::class) ]
      )
    ]
  )
  private val getAllProduct = AsyncRoute("/products", RouteMethod.GET) {
    val products = productUseCases.getAllProduct()
    response = fromProductList(products)
  }

  @OpenApi(
    path = "/products/{id}",
    methods = [HttpMethod.GET],
    summary = "Get Product",
    description = "Return product",
    tags = [ "Product" ],
    pathParams = [ OpenApiParam("id") ],
    responses = [
      OpenApiResponse(
        status = "200",
        description = "Product found!",
        content = [ OpenApiContent(from = ProductResponseDto::class) ]
      ),
      OpenApiResponse(
        status = "404",
        description = "Not found",
        content = [ OpenApiContent(from = ErrorResponse::class) ]
      )
    ]
  )
  private val getProductById = AsyncRoute("/products/{id}", RouteMethod.GET) {
    try {
      val id = ctx.pathParam("id")
      response = fromProduct(productUseCases.getProductById(UUID.fromString(id))!!)
    } catch (e: ProductNotFoundException) {
      ctx.error(ErrorResponse(404, e.detail))
    } catch (e: IllegalStateException) {
      ctx.error(ErrorResponse(404, e.stackTraceToString()))
    }
  }

  override val routes = setOf(getProductById, getAllProduct)
}
