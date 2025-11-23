package com.hng.notification_dispatcher.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queues.email}")
    private String emailQueue;

    @Value("${rabbitmq.queues.push}")
    private String pushQueue;

    @Value("${rabbitmq.queues.failed}")
    private String failedQueue;

    @Value("${rabbitmq.routing-keys.email}")
    private String emailRoutingKey;

    @Value("${rabbitmq.routing-keys.push}")
    private String pushRoutingKey;

    @Value("${rabbitmq.routing-keys.failed}")
    private String failedRoutingKey;

    @Bean
    public DirectExchange notificationsExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(emailQueue)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", failedRoutingKey)
                .withArgument("x-max-priority", 10)
                .build();
    }

    @Bean
    public Queue pushQueue() {
        return QueueBuilder.durable(pushQueue)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", failedRoutingKey)
                .withArgument("x-max-priority", 10)
                .build();
    }

    @Bean
    public Queue failedQueue() {
        return QueueBuilder.durable(failedQueue).build();
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(notificationsExchange())
                .with(emailRoutingKey);
    }

    @Bean
    public Binding pushBinding() {
        return BindingBuilder
                .bind(pushQueue())
                .to(notificationsExchange())
                .with(pushRoutingKey);
    }

    @Bean
    public Binding failedBinding() {
        return BindingBuilder
                .bind(failedQueue())
                .to(notificationsExchange())
                .with(failedRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
