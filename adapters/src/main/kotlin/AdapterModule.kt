package adapters

import adapters.config.AppConfig
import adapters.config.ConfigRepository
import adapters.config.ConfigRepositoryImpl
import adapters.config.EnvironmentVariables
import adapters.config.EnvironmentVariablesImpl
import adapters.database.DatabaseConnector
import adapters.database.DatabaseErrorInspector
import adapters.database.TransactionServiceDbImpl
import adapters.database.products.ProductRepositoryImpl
import adapters.database.products.ProductSQLRepository
import adapters.database.utils.PgErrorInspector
import adapters.healthcheck.HealthCheckService
import adapters.util.DateSupplierSystemTimeImpl
import adapters.web.config.JavalinWebServer
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.binds
import org.koin.dsl.module
import ports.input.DateSupplier
import ports.output.repository.ProductRepository
import ports.output.repository.TransactionService
import javax.sql.DataSource

// Environment-specific configuration
val envModule = module(createdAtStart = true) {
  single<EnvironmentVariables> {
    EnvironmentVariablesImpl()
  }
  single<DateSupplier> {
    DateSupplierSystemTimeImpl()
  }
}

val adapterModule = module(createdAtStart = true) {

  // Configuration
  single<ConfigRepository> {
    ConfigRepositoryImpl(envVars = get())
  }
  single {
    AppConfig(configRepository = get())
  }
  single {
    JavalinWebServer().start()
  }

  // Database
  single<DataSource> {
    // Data source. We use 1 data source per 1 database. One data source may supply multiple connections.
    HikariDataSource(get<AppConfig>().hikari)
  }
  single {
    DatabaseConnector(dataSource = get())
  }
  single<TransactionService> {
    TransactionServiceDbImpl(dbConnector = get())
  }

  // Data repositories
  single {
    ProductSQLRepository()
  }

  // Data adapters
  single {
    ProductRepositoryImpl(productSQLRepository = get())
  } binds arrayOf(
    ProductRepository::class
  )

  // Internal adapter services
  single {
    HealthCheckService(appConfig = get(), dateSupplier = get())
  }
  single<DatabaseErrorInspector> {
    PgErrorInspector()
  }
}
