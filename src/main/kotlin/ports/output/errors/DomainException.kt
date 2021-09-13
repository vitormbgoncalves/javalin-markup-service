package ports.output.errors

open class DomainException(
  val errorType: String,
  val title: String,
  val detail: String
) : RuntimeException(detail)
