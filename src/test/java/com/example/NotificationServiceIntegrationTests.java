package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Testcontainers
public class NotificationServiceIntegrationTests {

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka-native:3.8.0"));

    private static KafkaProducer<String, String> producer;

    private static KafkaConsumer<String, String> consumer;

    @BeforeAll
    public static void setUp() {
        String bootstrap = kafkaContainer.getBootstrapServers();
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(producerProperties);

        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-test-notifications");
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(List.of("actions-test"));
    }

    @Test
    @DisplayName("Получено сообщение CREATE")
    public void listenAccountEventTest_receivedCREATE() throws Exception {
        producer.send(new ProducerRecord<>("actions-test", "CREATE example@gmail.com")).get();
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));

        assertEquals(1, records.count());
        assertEquals("CREATE example@gmail.com", records.iterator().next().value());
    }

    @Test
    @DisplayName("Получено сообщение DELETE")
    public void listenAccountEventTest_receivedDELETE() throws Exception {
        producer.send(new ProducerRecord<>("actions-test", "DELETE example@gmail.com")).get();
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));

        assertEquals(1, records.count());
        assertEquals("DELETE example@gmail.com", records.iterator().next().value());
    }
}
