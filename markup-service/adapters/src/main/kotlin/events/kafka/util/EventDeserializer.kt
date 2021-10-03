package adapters.events.kafka.util

import adapters.events.EventFormat
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import java.text.SimpleDateFormat

class EventDeserializer : Deserializer<EventFormat> {

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  private val mapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .registerModule(JodaMoneyModule())
    .setDateFormat(dateFormat)

  override fun configure(configs: Map<String?, *>?, isKey: Boolean) {}

  override fun deserialize(topic: String?, data: ByteArray?): EventFormat? {
    return try {
      if (data == null) {
        println("Null received at deserializing")
        return null
      }
      println("Deserializing...")
      mapper.readValue<EventFormat>(data)
    } catch (e: Exception) {
      throw SerializationException("Error when deserializing byte[] to EventFormat")
    }
  }

  override fun close() {}
}
