package org.example.rabbitmqdemo.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnBooleanProperty(value = "rabbit.producer.enabled", havingValue = false)
public class RabbitMQListener {

    @Value("${rabbit.producer.enabled}")
    public boolean producerEnabled;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @Value("${spring.rabbitmq.mqtt-routing-key}")
    private String mqttRoutingKey;

    @Value("${spring.rabbitmq.mqtt-exchange}")
    private String mqttExchange;

    @Value("${spring.rabbitmq.mqtt-queue}")
    private String mqttQueue;




//    private final Tracer tracer;
//
//    public RabbitMQListener(OpenTelemetry openTelemetry) {
//        this.tracer = openTelemetry.getTracer(getClass().getSimpleName());
//    }

    @WithSpan
    @RabbitListener(queues = "#{ @queue }")
    public void process(Message message) throws InterruptedException {
        byte[] messageBodyBytes = message.getBody();
        Thread.sleep(500 * 2);
        String messageBody = new String(messageBodyBytes);
        log.info("RabbitMQListener:\nAMQP \nProperties: {}\nBody: {}\n\n", message.getMessageProperties(), messageBody);
    }

    @WithSpan
    @RabbitListener(queues = "#{ @mqttQueue }")
    public void processMQTT(Message message) throws InterruptedException {
        byte[] messageBodyBytes = message.getBody();
        Thread.sleep(500 * 2);
        String messageBody = new String(messageBodyBytes);
        log.info("RabbitMQListener:\nMQTT \nProperties: {}\nBody: {}\n\n", message.getMessageProperties(), messageBody);
    }

}
