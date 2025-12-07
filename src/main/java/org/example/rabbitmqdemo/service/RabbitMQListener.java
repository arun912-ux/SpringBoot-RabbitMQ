package org.example.rabbitmqdemo.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQListener {


    @RabbitListener(queues = "my-queue")
    public void process(String message) {
        log.info("RabbitMQListener: {}", message);
    }

}
