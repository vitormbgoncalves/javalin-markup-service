package adapters.web.routes.products

import adapters.web.config.AsyncRoute
import adapters.web.config.AsyncRoutes
import adapters.web.routes.products.dto.ProductListResponseDto
import adapters.web.routes.products.dto.ProductListResponseDto.Companion.fromProductList
import adapters.web.routes.products.dto.ProductRequestDto
import adapters.web.routes.products.dto.ProductResponseDto
import adapters.web.routes.products.dto.ProductResponseDto.Companion.fromProduct
import com.reposilite.web.http.ErrorResponse
import com.reposilite.web.routing.RouteMethod.DELETE
import com.reposilite.web.routing.RouteMethod.GET
import com.reposilite.web.routing.RouteMethod.POST
import com.reposilite.web.routing.RouteMethod.PUT
import io.javalin.openapi.HttpMethod
import io.javalin.openapi.OpenApi
import io.javalin.openapi.OpenApiContent
import io.javalin.openapi.OpenApiRequestBody
import io.javalin.openapi.OpenApiResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ports.input.ProductUseCases
import java.util.UUID

internal class ProductRoutes : KoinComponent, AsyncRoutes() {

  private val productUseCases by inject<ProductUseCases>()

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
        status = "401",
        description = "Not found",
        content = [ OpenApiContent(from = IllegalStateException::class) ]
      )
    ],
    requestBody = OpenApiRequestBody(content = [ OpenApiContent(from = ProductRequestDto::class) ])
  )
  private val addProduct = AsyncRoute("/products", POST) {
    val product = ctx.bodyAsClass<ProductRequestDto>().toProduct(null)
    response = productUseCases.addProduct(product)
  }

  @OpenApi(
    path = "/products/{id}",
    methods = [HttpMethod.GET],
    summary = "Get Product",
    description = "Return product",
    tags = [ "Product" ],
    responses = [
      OpenApiResponse(
        status = "200",
        description = "Product found!",
        content = [ OpenApiContent(from = ProductResponseDto::class) ]
      ),
      OpenApiResponse(
        status = "401",
        description = "Not found",
        content = [ OpenApiContent(from = ErrorResponse::class) ]
      )
    ]
  )
  private val getProductById = AsyncRoute("/products/{id}", GET) {
    val product = productUseCases.getProductById(UUID.fromString(ctx.pathParam("id")))
    response = fromProduct(product!!)
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
        status = "401",
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
    methods = [HttpMethod.PUT],
    summary = "Update Product",
    description = "Update product",
    tags = [ "Product" ],
    responses = [
      OpenApiResponse(
        status = "200",
        description = "Product updated!",
        content = [ OpenApiContent(from = ProductResponseDto::class) ]
      ),
      OpenApiResponse(
        status = "401",
        description = "Not found",
        content = [ OpenApiContent(from = ErrorResponse::class) ]
      )
    ]
  )
  private val updateProduct = AsyncRoute("/products/{id}", PUT) {
    val id = UUID.fromString(ctx.pathParam("id"))
    val product = ctx.bodyAsClass<ProductRequestDto>().toProduct(id)
    response = productUseCases.updateProduct(id, product)
  }

  @OpenApi(
    path = "/products/{id}",
    methods = [HttpMethod.DELETE],
    summary = "Delete Product",
    description = "Delete product",
    tags = [ "Product" ],
    responses = [
      OpenApiResponse(
        status = "200",
        description = "Product deleted!",
        content = [ OpenApiContent(from = ProductResponseDto::class) ]
      ),
      OpenApiResponse(
        status = "401",
        description = "Not found",
        content = [ OpenApiContent(from = ErrorResponse::class) ]
      )
    ]
  )
  private val deleteProduct = AsyncRoute("/products/{id}", DELETE) {
    val id = UUID.fromString(ctx.pathParam("id"))
    response = productUseCases.deleteProduct(id)
  }

  override val routes = setOf(addProduct, getProductById, getAllProduct, updateProduct, deleteProduct)
}
