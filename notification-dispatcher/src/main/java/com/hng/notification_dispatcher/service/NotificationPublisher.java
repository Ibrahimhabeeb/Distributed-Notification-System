package com.hng.notification_dispatcher.service;
import com.hng.dtos.ContactInfo;
import com.hng.dtos.NotificationRequest;
import com.hng.dtos.NotificationType;
import com.hng.dtos.UserDetailsDto;
import com.hng.notification_dispatcher.config.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;
@Service
@Slf4j
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private UserClient userClient;

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
            UserDetailsDto user = getUserDetails(request.getUserId());
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setEmail(user.getEmail());
//            contactInfo.setEmail("ibrahim.habeeb2004@gmail.com");
            contactInfo.setPushToken(user.getPushToken());
            request.setContactInfo(contactInfo);

            String newRequestId = UUID.randomUUID().toString();
            request.setRequestId(newRequestId);

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

            log.info("Successfully published notification with request_id: {}", request);

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



    @CircuitBreaker(name= "userService")
    private UserDetailsDto getUserDetails(UUID id){

        return   userClient.getUserDetails(id.toString());

    }






}
