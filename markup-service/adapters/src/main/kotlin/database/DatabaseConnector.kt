package database

import database.products.Currency
import database.products.Products
import javax.sql.DataSource
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransaction
import output.repository.RequiresTransactionContext

class DatabaseConnector(
  dataSource: DataSource,
  private val preInitHandler: (DatabaseConnector.() -> Unit)? = null
) {
  private val db: Database = Database.connect(datasource = dataSource)

  private val tables = arrayOf(Products, Currency)

  init {
    runBlocking {
      @OptIn(RequiresTransactionContext::class)
      newTransaction {
        preInitHandler?.invoke(this@DatabaseConnector)
        SchemaUtils.create(*tables)
      }
    }
  }

  @RequiresTransactionContext
  suspend fun deleteAllTables() {
    existingTransaction {
      SchemaUtils.drop(*tables)
    }
  }

  @RequiresTransactionContext
  suspend fun <T> newTransaction(
    block: suspend (tx: Transaction) -> T
  ): T {
    return newSuspendedTransaction(db = db) {
      block(this)
    }
  }

  @RequiresTransactionContext
  suspend fun <T> existingTransaction(block: suspend (tx: Transaction) -> T): T {
    val tx = TransactionManager.current()
    return tx.suspendedTransaction {
      block(this)
    }
  }

  @RequiresTransactionContext
  suspend fun <T> transaction(block: suspend (tx: Transaction) -> T): T {
    val tx = TransactionManager.currentOrNull()
    return if (tx == null || tx.connection.isClosed) {
      newSuspendedTransaction(db = db) {
        block(this)
      }
    } else {
      tx.suspendedTransaction {
        block(this)
      }
    }
  }
}
