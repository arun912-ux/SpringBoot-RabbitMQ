package org.example.rabbitmqdemo.controller;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.trace.Span;
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
    private final ObservationRegistry observationRegistry;

    public WebController(RabbitMQProducer rabbitMQProducer, ObservationRegistry observationRegistry) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.observationRegistry = observationRegistry;
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
    @Observed(name = "WebController.send")
    public String send(@RequestParam(required = false) Optional<String> var) throws InterruptedException {
        Thread.sleep(1000L);
        log.info("inside send method");

        Span currentSpan = Span.current();
        currentSpan.setAttribute("var", var.orElse("DEFAULT"));

        Message message = rabbitMQProducer.buildMessage(var.orElse("DEFAULT"), UUID.randomUUID().toString());
        return "sent : " + new String(message.getBody());
    }

}
