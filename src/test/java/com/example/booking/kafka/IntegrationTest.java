package com.example.booking.kafka;

import kafka.Kafka;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

@Disabled
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {

    private static final Network network = Network.newNetwork();

    private static final GenericContainer<?> ZOOKEEPER_CONTAINER = new GenericContainer<>(DockerImageName.parse("confluentinc/cp-zookeeper:latest"))
            .withNetwork(network)
            .withNetworkAliases("zookeeper")
            .withExposedPorts(2181);

    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .withNetwork(network)
            .withNetworkAliases("kafka")
            .withExposedPorts(9092)
            .waitingFor(Wait.forListeningPort());

    @BeforeAll
    public static void setupContainers() {
        // Zookeeper 컨테이너 시작
        ZOOKEEPER_CONTAINER.start();

        // Kafka 컨테이너 시작
        KAFKA_CONTAINER.start();
    }

    @AfterAll
    public static void stopContainers() {
        // Kafka 컨테이너 중지
        KAFKA_CONTAINER.stop();

        // Zookeeper 컨테이너 중지
        ZOOKEEPER_CONTAINER.stop();
    }

    static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            Map<String, String> properties = new HashMap<>();

            // Kafka 연결 정보 설정
            setKafkaProperties(properties);

            // 애플리케이션 컨텍스트에 속성값 적용
            TestPropertyValues.of(properties).applyTo(applicationContext);
        }

        // Kafka 연결 정보 설정
        private void setKafkaProperties(Map<String, String> properties) {
            properties.put("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
        }
    }
}