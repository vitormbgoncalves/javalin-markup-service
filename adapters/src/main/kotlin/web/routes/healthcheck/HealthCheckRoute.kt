package adapters.web.routes.healthcheck

import adapters.healthcheck.HealthCheckService
import adapters.web.config.AsyncRoute
import adapters.web.config.AsyncRoutes
import com.reposilite.web.routing.RouteMethod.GET
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class HealthCheckRoute : KoinComponent, AsyncRoutes() {

  private val healthCheckService by inject<HealthCheckService>()

  private val healthCheck = AsyncRoute("/health", GET) {
    response = healthCheckService.status()
  }

  override val routes = setOf(healthCheck)
}
