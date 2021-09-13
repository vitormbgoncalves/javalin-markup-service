package adapters.database.utils

import adapters.database.DatabaseErrorInspector
import adapters.database.DatabaseErrorState
import org.jetbrains.exposed.exceptions.ExposedSQLException

internal class PgErrorInspector : DatabaseErrorInspector {
  override fun errorState(e: ExposedSQLException): DatabaseErrorState = when (e.sqlState) {
    "23505" -> DatabaseErrorState.DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT
    else -> DatabaseErrorState.OTHER
  }
}
