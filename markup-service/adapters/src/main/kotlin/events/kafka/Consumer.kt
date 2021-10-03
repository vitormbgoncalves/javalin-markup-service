package adapters.events.kafka

import adapters.events.EventFormat
import adapters.events.fromBrokerEvent
import adapters.events.kafka.util.EventDeserializer
import config.ConfigRepository
import entities.Product
import input.ProductUseCases
import io.streamthoughts.kafka.clients.consumer.AutoOffsetReset
import io.streamthoughts.kafka.clients.consumer.ConsumerTask
import io.streamthoughts.kafka.clients.consumer.ConsumerWorker
import io.streamthoughts.kafka.clients.consumer.error.closeTaskOnConsumedError
import io.streamthoughts.kafka.clients.consumer.error.serialization.replaceWithNullOnInvalidRecord
import io.streamthoughts.kafka.clients.consumer.listener.forEach
import io.streamthoughts.kafka.clients.kafka
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import web.config.ContextDsl

object Consumer : KoinComponent {

  private val configRepository by inject<ConfigRepository>()
  private val bootstrapServers = configRepository.config.getConfig("app-config").getString("kafka.bootstrapServers")
  private val logger = LogManager.getLogger()

  @OptIn(DelicateCoroutinesApi::class)
  private fun consumerWorker(productUseCases: ProductUseCases): ConsumerWorker<String, EventFormat> = kafka(bootstrapServers) {
    client {
      clientId("product-markup")
    }

    consumer("my-group", StringDeserializer(), EventDeserializer()) {
      configure {
        pollRecordsMs(500)
        maxPollRecords(1000)
        autoOffsetReset(AutoOffsetReset.Earliest)
      }

      onDeserializationError(replaceWithNullOnInvalidRecord())

      onConsumedError(closeTaskOnConsumedError())

      onPartitionsAssigned { _: Consumer<*, *>, partitions ->
        println("Partitions assigned: $partitions")
      }

      onPartitionsRevokedAfterCommit { _: Consumer<*, *>, partitions ->
        println("Partitions revoked: $partitions")
      }

      onConsumed(
        forEach { _: ConsumerTask, value: EventFormat? ->
          if (value != null) {

            logger.warn("consumed record-value: ${value.data.manufacturer}")

            runBlocking {
              when (value.eventType) {
                "insert" -> productUseCases.addProduct(Product.fromBrokerEvent(value))
                else -> throw IllegalArgumentException("not found event persistence!!!")
              }
            }
          } else {
            println("consumed record-value null")
          }
        }
      )
    }
  }

  internal fun message(productUseCases: ProductUseCases): ConsumerWorker<String, EventFormat> = consumerWorker(productUseCases)
}

// fun kafkaConsumer(): KafkaConsumer<String, EventFormat> {
//  val configs = consumerConfigsOf()
//    .client { bootstrapServers("localhost:9092") }
//    .groupId("my-group")
//    .keyDeserializer(StringDeserializer::class.java.name)
//    .valueDeserializer(EventFormat::class.java.name)
//
//  return KafkaConsumer<String, EventFormat>(configs)
//
//  consumer.use {
//    consumer.subscribe(listOf("markup-service"))
//    while (true) {
//      consumer
//        .poll(Duration.ofMillis(500))
//        .forEach { record ->
//          return record
//        }
//    }
//  }
// }

class SuspendHandler(
  handler: suspend ContextDsl.() -> Unit
)

/*
@OptIn(RequiresTransactionContext::class)
private val consumerWorker : KafkaConsumerWorker.Builder<String, EventFormat> = kafka(adapters.events.kafka.Consumer.bootstrapServers) {
  client {
    clientId("product-markup")
  }

  consumer("my-group", StringDeserializer(), EventDeserializer()) {
    configure {
      pollRecordsMs(500)
      maxPollRecords(1000)
      autoOffsetReset(AutoOffsetReset.Earliest)
    }

    onDeserializationError(replaceWithNullOnInvalidRecord())

    onConsumedError(closeTaskOnConsumedError())

    onPartitionsAssigned { _: Consumer<*, *>, partitions ->
      println("Partitions assigned: $partitions")
    }

    onPartitionsRevokedAfterCommit { _: Consumer<*, *>, partitions ->
      println("Partitions revoked: $partitions")
    }

    onConsumed(
      forEach { _: ConsumerTask, value: EventFormat? ->
        if (value != null) {
          println("consumed record-value: ${value.data.name}")
          adapters.events.kafka.Consumer.logger.warn("consumed record-value: $value")

          when (value.eventType) {
            "insert" -> adapters.events.kafka.Consumer.productUseCases.addProduct(Product.fromBrokerEvent(value))
            else -> throw IllegalArgumentException("not found event persistence!!!")
          }

        } else {
          println("consumed record-value null")
        }
      }
    )
  }
}*/

/*    internal fun message(): ConsumerWorker<String, EventFormat> {
      consumerWorker.start("markup-service", maxParallelHint = 2)
      logger.warn("Producer started...")
      return consumerWorker
    }*/
