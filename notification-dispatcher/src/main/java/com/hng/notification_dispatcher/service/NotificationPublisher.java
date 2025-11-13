package com.hng.notification_dispatcher.service;
import com.hng.notification_dispatcher.model.NotificationRequest;
import com.hng.notification_dispatcher.model.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing-keys.email}")
    private String emailRoutingKey;

    @Value("${rabbitmq.routing-keys.push}")
    private String pushRoutingKey;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNotification(NotificationRequest request) {
        try {
            String routingKey = getRoutingKey(request.getNotificationType());

            log.info("Publishing {} notification with request_id: {} to routing key: {}",
                    request.getNotificationType(),
                    request.getRequestId(),
                    routingKey);

            rabbitTemplate.convertAndSend(
                    exchangeName,
                    routingKey,
                    request,
                    message -> {
                        message.getMessageProperties().setPriority(request.getPriority());
                        message.getMessageProperties().setHeader("request_id", request.getRequestId());
                        message.getMessageProperties().setHeader("user_id", request.getUserId().toString());

                        return message;
                    }
            );

            log.info("Successfully published notification with request_id: {}", request.getRequestId());

        } catch (Exception e) {
            log.error("Failed to publish notification with request_id: {}", request.getRequestId(), e);

        }
    }

    private String getRoutingKey(NotificationType type) {
        return switch (type) {
            case email -> emailRoutingKey;
            case push -> pushRoutingKey;
        };
    }





}
