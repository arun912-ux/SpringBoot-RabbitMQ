package org.example.rabbitmqdemo.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmqdemo.service.RabbitMQProducer;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
public class WebController {

    private final RabbitMQProducer rabbitMQProducer;

    public WebController(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @GetMapping
    public String index() {
        return "index";
    }


    @Observed
    @GetMapping("home")
    @ResponseBody
    public String home() throws InterruptedException {
        Thread.sleep(2000L);
        return "home";
    }

    @GetMapping("send")
    @ResponseBody
    public String send(@RequestParam(required = false) Optional<String> var) throws InterruptedException {
        Thread.sleep(1000L);
        log.info("inside send method");

        Message message = rabbitMQProducer.buildMessage(var.orElse("DEFAULT"), UUID.randomUUID().toString());
        return "sent : " + new String(message.getBody());
    }

}
