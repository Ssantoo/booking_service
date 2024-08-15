package com.example.booking.kafka;

import com.example.booking.support.kafka.service.KafkaConsumer;
import com.example.booking.support.kafka.service.KafkaProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 3, brokerProperties = {"listeners=PLAINTEXT://localhost:9093"}, ports = { 9093 })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KafkaTest {

    private static final Logger log = LoggerFactory.getLogger(KafkaTest.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String receivedMessage;

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "testTopic")
    public void listen(String message) {
        log.info("받은 메세지: " + message);
        receivedMessage = message;
        latch.countDown();
    }

    @Test
    public void 카프카_메세지_전송_테스트() throws InterruptedException {
        String msg = "테스트 메세지";
        log.info("보낸 메세지 : '{}'", msg);
        kafkaProducer.publish("testTopic", 1L, msg);
        kafkaTemplate.flush();

        boolean messageReceived = latch.await(5, TimeUnit.SECONDS);

        assertThat(messageReceived).isTrue();
        log.info("받은 메세지 : '{}'", receivedMessage);
        assertThat(receivedMessage).isEqualTo(msg);
    }


}
