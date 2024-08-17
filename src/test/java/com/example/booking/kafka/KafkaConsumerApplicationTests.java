package com.example.booking.kafka;

import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import com.example.booking.support.kafka.service.KafkaConsumer;
import com.example.booking.support.kafka.service.KafkaProducer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Order(0)
@DirtiesContext
public class KafkaConsumerApplicationTests extends IntegrationTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private KafkaConsumer kafkaConsumer;

    // 카프카 메시지 전송 및 수신 테스트
    @Test
    public void kafkaSendAndConsumeTest() {
        String topic = "test-topic";
        String msg = "expect-value";

        kafkaProducer.publish(topic, 1L ,msg);

        var stringCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(kafkaConsumer, Mockito.timeout(5000).times(1))
                .listen(stringCaptor.capture());

        assertThat(msg).isEqualTo(stringCaptor.getValue());
    }
}
