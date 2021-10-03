package adapters

import com.zaxxer.hikari.HikariDataSource
import config.AppConfig
import config.ConfigRepository
import config.ConfigRepositoryImpl
import database.DatabaseConnector
import database.TransactionServiceDbImpl
import database.products.ProductRepositoryImpl
import database.products.ProductSQLRepository
import org.koin.dsl.binds
import org.koin.dsl.module
import output.repository.ProductRepository
import output.repository.TransactionService
import web.config.JavalinWebServer
import javax.sql.DataSource

val adapterModule = module(createdAtStart = true) {

  // Configuration
  single<ConfigRepository> {
    ConfigRepositoryImpl()
  }
  single {
    AppConfig(configRepository = get())
  }

  // Javalin server start
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
}
