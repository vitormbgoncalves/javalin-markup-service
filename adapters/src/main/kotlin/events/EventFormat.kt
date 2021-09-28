package adapters.events

import java.sql.Timestamp

class EventFormat(
  val eventType: String,
  val data: PublishEventDTO,
  val timestamp: Timestamp
) {
  companion object
}
