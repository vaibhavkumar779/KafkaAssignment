package com.knoldus

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import java.util.Scanner
import io.circe.syntax.EncoderOps
import io.circe.generic.auto._
object KafkaScalaProducer extends App {

  val scanner = new Scanner(System.in)

  val props = new Properties()

  /**
   * bootstrap.servers
   * A list of host/port pairs to use for establishing the initial connection to the Kafka cluster.
   */
  props.put("bootstrap.servers", "localhost:9092")

  props.put("client.id", "ScalaProducerExample")

  props.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")

  props.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")

  props.put("acks", "1")

  /**
   * retries
   * Setting a value greater than zero will cause the client to resend any record whose send
   * fails with a potentially transient error.
   */
  props.put("retries", "0")

  props.put("batch.size", "16384")

  props.put("linger.ms", "1")

  props.put("buffer.memory", "33554432")

  /**
   * A producer is instantiated by providing the configuration.
   */
  val producer: KafkaProducer[String, String] =
    new KafkaProducer[String, String](props)

  /**
   * The topic where record should be sent to.
   */
  val topic = "kafka-topic-kip"

  println(s"Sending Records in Kafka Topic [$topic]")

  for (i <- 1 to 10) {

    /**
     * Creates a record to be sent to a specified topic and partition
     */
    val record: ProducerRecord[String, String] =
      new ProducerRecord(topic,
        i.toString,
        createUserMessage(i).asJson.toString)
    println(record)

    producer.send(record)
  }

  /**
   * Close this producer. This method blocks until all previously sent requests complete.
   */
  producer.close()

  /**
   * Method for creating the user message for every record

   */
  def createUserMessage(id: Int): Student = {
    var name = ""
    var course = ""
    var age = 0
    println("Enter the name")
    name = scanner.nextLine()

    println("Enter the course")
    course = scanner.nextLine()

    println("Enter the age")
    age = scanner.nextInt()
    scanner.nextLine()

    Student(id, name, course, age)

  }
}
/*
 * Case class for implement the user user details field
 */
case class Student(id: Int, name: String, course: String, age: Int)

