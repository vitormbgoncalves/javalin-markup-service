package adapters.web.routes.products

import adapters.events.EventFormat
import adapters.events.PublishEventDTO
import adapters.events.PublishEventDTO.Companion.fromProductResponseDto
import adapters.events.kafka.Producer.message
import adapters.web.config.AsyncRoute
import adapters.web.config.AsyncRoutes
import adapters.web.routes.products.dto.ProductListResponseDto
import adapters.web.routes.products.dto.ProductListResponseDto.Companion.fromProductList
import adapters.web.routes.products.dto.ProductRequestDto
import adapters.web.routes.products.dto.ProductResponseDto
import adapters.web.routes.products.dto.ProductResponseDto.Companion.fromProduct
import com.reposilite.web.http.ErrorResponse
import com.reposilite.web.http.error
import com.reposilite.web.routing.RouteMethod.DELETE
import com.reposilite.web.routing.RouteMethod.GET
import com.reposilite.web.routing.RouteMethod.POST
import com.reposilite.web.routing.RouteMethod.PUT
import io.javalin.openapi.HttpMethod
import io.javalin.openapi.OpenApi
import io.javalin.openapi.OpenApiContent
import io.javalin.openapi.OpenApiParam
import io.javalin.openapi.OpenApiRequestBody
import io.javalin.openapi.OpenApiResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ports.input.ProductUseCases
import ports.output.repository.ProductNotFoundException
import java.sql.Timestamp
import java.util.UUID

internal class ProductRoutes() : KoinComponent, AsyncRoutes() {

  private val productUseCases by inject<ProductUseCases>()
  private val producer = message()

  @OpenApi(
    path = "/products",
    methods = [HttpMethod.POST],
    summary = "Create Product",
    description = "Return new product registered",
    tags = [ "Product" ],
    responses = [
      OpenApiResponse(
        status = "200",
        description = "Product registration ok!",
        content = [ OpenApiContent(from = ProductResponseDto::class) ]
      ),
      OpenApiResponse(
        status = "404",
        description = "Not found",
        content = [ OpenApiContent(from = IllegalStateException::class) ]
      )
    ],
    requestBody = OpenApiRequestBody(content = [ OpenApiContent(from = ProductRequestDto::class) ])
  )
  private val addProduct = AsyncRoute("/products", POST) {
    val requestBody = ctx.bodyAsClass<ProductRequestDto>().addProduct()
    val registration = productUseCases.addProduct(requestBody)
    val responseBody: ProductResponseDto = fromProduct(registration)
    response = responseBody
    producer.send(
      key = "addProduct",
      value = EventFormat(
        eventType = "insert",
        data = fromProductResponseDto(responseBody),
        timestamp = Timestamp(System.currentTimeMillis())
      )
    )
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
  private val getAllProduct = AsyncRoute("/products", GET) {
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
  private val getProductById = AsyncRoute("/products/{id}", GET) {
    try {
      val id = ctx.pathParam("id")
      response = fromProduct(productUseCases.getProductById(UUID.fromString(id))!!)
    } catch (e: ProductNotFoundException) {
      ctx.error(ErrorResponse(404, e.detail))
    } catch (e: IllegalStateException) {
      ctx.error(ErrorResponse(404, e.stackTraceToString()))
    }
  }

  @OpenApi(
    path = "/products/{id}",
    methods = [HttpMethod.PUT],
    summary = "Update Product",
    description = "Update product",
    tags = [ "Product" ],
    pathParams = [ OpenApiParam("id") ],
    responses = [
      OpenApiResponse(
        status = "200",
        description = "Product updated!",
        content = [ OpenApiContent(from = ProductResponseDto::class) ]
      ),
      OpenApiResponse(
        status = "404",
        description = "Not found",
        content = [ OpenApiContent(from = ErrorResponse::class) ]
      )
    ],
    requestBody = OpenApiRequestBody(content = [ OpenApiContent(from = ProductRequestDto::class) ])
  )
  private val updateProduct = AsyncRoute("/products/{id}", PUT) {
    try {
      val id = UUID.fromString(ctx.pathParam("id"))
      val requestBody = ctx.bodyAsClass<ProductRequestDto>().updateProduct(id)
      val update = productUseCases.updateProduct(requestBody)
      val responseBody: ProductResponseDto = fromProduct(update!!)
      response = responseBody
      producer.send(
        key = "updateProduct",
        value = EventFormat(
          eventType = "update",
          data = fromProductResponseDto(responseBody),
          timestamp = Timestamp(System.currentTimeMillis())
        )
      )
    } catch (e: ProductNotFoundException) {
      ctx.error(ErrorResponse(404, e.detail))
    }
  }

  @OpenApi(
    path = "/products/{id}",
    methods = [HttpMethod.DELETE],
    summary = "Delete Product",
    description = "Delete product",
    tags = [ "Product" ],
    pathParams = [ OpenApiParam("id") ],
    responses = [
      OpenApiResponse(
        status = "204",
        description = "Product deleted!",
        content = [ OpenApiContent(from = Unit::class) ]
      ),
      OpenApiResponse(
        status = "404",
        description = "Not found",
        content = [ OpenApiContent(from = ErrorResponse::class) ]
      )
    ]
  )
  private val deleteProduct = AsyncRoute("/products/{id}", DELETE) {
    try {
      val id = UUID.fromString(ctx.pathParam("id"))
      response = productUseCases.deleteProduct(id)
      ctx.status(204)
      producer.send(
        key = "deleteProduct",
        value = EventFormat(
          eventType = "delete",
          data = PublishEventDTO(id = id),
          timestamp = Timestamp(System.currentTimeMillis())
        )
      )
    } catch (e: ProductNotFoundException) {
      ctx.error(ErrorResponse(404, e.detail))
    }
  }

  override val routes = setOf(addProduct, getProductById, getAllProduct, updateProduct, deleteProduct)
}
