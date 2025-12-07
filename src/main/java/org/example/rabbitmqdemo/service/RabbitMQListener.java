package org.example.rabbitmqdemo.service;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQListener {

//    private final Tracer tracer;
//
//    public RabbitMQListener(OpenTelemetry openTelemetry) {
//        this.tracer = openTelemetry.getTracer(getClass().getSimpleName());
//    }

    @WithSpan
    @RabbitListener(queues = "my-queue")
    public void process(Message message) {
        log.info("RabbitMQListener: {}\n\n", message);
    }


}
