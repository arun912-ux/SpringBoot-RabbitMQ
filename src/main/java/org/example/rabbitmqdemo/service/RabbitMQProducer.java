package org.example.rabbitmqdemo.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @WithSpan
    @Scheduled(fixedRate = 1_000 * 10)
    public void send() {
        Message message = new Message("Hello World".getBytes());
        log.info("send message: {}\n", message);
        rabbitTemplate.send("my-exchange", "my-routing-key", message);
    }
}
