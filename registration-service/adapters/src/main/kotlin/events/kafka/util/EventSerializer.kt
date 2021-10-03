package adapters.events.kafka.util

import adapters.events.EventFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.text.SimpleDateFormat
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer

class EventSerializer : Serializer<EventFormat> {

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  private val mapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .registerModule(JodaMoneyModule())
    .setDateFormat(dateFormat)
    .enable(SerializationFeature.INDENT_OUTPUT)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)

  override fun configure(configs: Map<String?, *>?, isKey: Boolean) {}

  override fun serialize(topic: String?, data: EventFormat?): ByteArray? {
    return try {
      if (data == null) {
        println("Null received at serializing")
        return null
      }
      println("Serializing...")
      mapper.writeValueAsBytes(data)
    } catch (e: Exception) {
      throw SerializationException("Error when serializing MessageDto to byte[]")
    }
  }

  override fun close() {}
}
