package org.example.rabbitmqdemo.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUser;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;


    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(exchange, true, false);
    }

//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange("my-topic-exchange");
//    }

//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange("my-direct-exchange");
//    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey).noargs();
    }

//    @Bean
    public JacksonJsonMessageConverter messageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

//    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cf = new CachingConnectionFactory(rabbitHost);
        cf.setUsername(rabbitUser);
        cf.setPassword(rabbitPassword);
//        cf.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        cf.setChannelCacheSize(25);
        return cf;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory
//            , JacksonJsonMessageConverter messageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
