package org.example.rabbitmqdemo.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@ConditionalOnBooleanProperty(value = "rabbit.producer.enabled", havingValue = true)
public class RabbitMQProducer {

    @Value("${rabbit.producer.enabled}")
    private boolean producerEnabled;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;


    private Integer counter = 0;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @WithSpan
    @Scheduled(fixedRate = 1_000L * 1/4)
    public void sendToRabbitMQ() {
        if (producerEnabled) {

            String uuid = UUID.randomUUID().toString();
            String messageString = counter + " - " + uuid + " - Hello World";
            Message message = buildMessage(messageString, uuid);
            log.info("send message: {} - {}\n", counter, message);
            rabbitTemplate.send(exchange, routingKey, message);
//            rabbitTemplate.sendAndReceive("my-exchange", "my-routing-key", message);
            counter++;

        }
    }

    private Message buildMessage(String messageString, String uuid) {
        MessageProperties properties = new MessageProperties();
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        properties.setCorrelationId(uuid);
        properties.setContentLength(messageString.getBytes().length);
        properties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
//        properties.setDeliveryTag(1);
        return new Message(messageString.getBytes(), properties);

    }
}
