package com.knoldus
import java.io.{BufferedWriter, File, FileWriter}
import java.util.{Collections, Properties}
import scala.collection.JavaConverters._
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

object KafkaScalaConsumer extends App {

  val props = new Properties()

  props.put("bootstrap.servers", "localhost:9092")

  props.put(
    "key.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer"
  )

  /**
   * Deserializer class for value that implements the Deserializer interface.
   */
  props.put(
    "value.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer"
  )

  props.put("group.id", "consumer-group-1")

  props.put("enable.auto.commit", "true")

  props.put("auto.commit.interval.ms", "1000")
  props.put("auto.offset.reset", "earliest")

  props.put("session.timeout.ms", "30000")

  val topic = "kafka-topic-kip"

  /**
   * A consumer is instantiated by providing the configuration.
   */
  val consumer: KafkaConsumer[Nothing, String] =
    new KafkaConsumer[Nothing, String](props)

  /**
   * Subscribe to the given list of topics to get dynamically assigned partitions.
   */
  consumer.subscribe(Collections.singletonList(topic))
  println("Consuming")

  /**
   * Infinite loop to read from topic as soon as it gets the record
   */
  while (true) {

    /**
     * Fetch data for the topics or partitions specified using one of the subscribe/assign APIs.
     * Add the consume data in to the file
     */
    val records: ConsumerRecords[Nothing, String] = consumer.poll(100)
    for (record <- records.asScala) {
      val bw = new BufferedWriter(new FileWriter("target/student.txt", true))
      bw.write("\n" + record.value)
      bw.close
      println(record.value)
    }
  }

}
