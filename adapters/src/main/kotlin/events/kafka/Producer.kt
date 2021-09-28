package adapters.events.kafka

import adapters.config.ConfigRepository
import adapters.events.EventFormat
import adapters.events.kafka.util.ProductResponseDtoSerializer
import io.streamthoughts.kafka.clients.kafka
import io.streamthoughts.kafka.clients.producer.Acks
import io.streamthoughts.kafka.clients.producer.ProducerContainer
import io.streamthoughts.kafka.clients.producer.callback.closeOnErrorProducerSendCallback
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object Producer : KoinComponent {

  private val configRepository by inject<ConfigRepository>()
  private val bootstrapServers = configRepository.config.getConfig("app-config").getString("kafka.bootstrapServers")
  private val logger = LogManager.getLogger()

  private val producerContainer: ProducerContainer<String, EventFormat> = kafka(bootstrapServers) {

    client {
      clientId("product-registration")
    }

    producer {
      configure {
        acks(Acks.InSyncReplicas)
        enableIdempotence(true)
        this.maxInFlightRequestsPerConnection(5)
        retries(2)
      }
      keySerializer(StringSerializer())
      valueSerializer(ProductResponseDtoSerializer())

      defaultTopic("markup-service")

      onSendError(closeOnErrorProducerSendCallback())

      onSendSuccess { _, _, metadata ->
        println("Record was sent successfully: topic=${metadata.topic()}, partition=${metadata.partition()}, offset=${metadata.offset()} ")
      }
    }
  }

  internal fun message(): ProducerContainer<String, EventFormat> {
    logger.info("Kafka connection state: ${producerContainer.state()}")
    return when (producerContainer.state()) {
      ProducerContainer.State.STARTED -> producerContainer
      else -> {
        producerContainer.init()
        producerContainer
      }
    }
  }
}
