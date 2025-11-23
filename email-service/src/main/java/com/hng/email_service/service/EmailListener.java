package com.hng.email_service.service;
import com.hng.email_service.dtos.NotificationObject;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailListener {


    private  final EmailService emailProcessingService;

    @RabbitListener(queues = "${rabbitmq.queues.email}")
    public void handleEmailNotification(
            @Payload NotificationObject message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        log.info("Received notification message: requestId={}, userId={}, template={}",
                message.getRequestId(), message.getUserId(), message.getTemplateCode());

        try {

            if (!"email".equalsIgnoreCase(message.getNotificationType())) {
                log.warn("Ignoring non-email notification: {}", message.getNotificationType());
                channel.basicAck(deliveryTag, false);
                return;
            }

            if (message.getContactInfo() == null ||
                    message.getContactInfo().getEmail() == null) {
                log.error("Missing email address in notification");
                channel.basicNack(deliveryTag, false, false); // Send to DLQ
                return;
            }

            // Process the notification
            emailProcessingService.processNotification(message);

            // Acknowledge successful processing
            channel.basicAck(deliveryTag, false);
            log.info("Successfully processed and acknowledged message: {}",
                    message.getRequestId());

        } catch (Exception e) {
            log.error("Error processing notification message: {}", e.getMessage(), e);

            try {
                // Reject message and send to DLQ
                channel.basicNack(deliveryTag, false, false);
                log.warn("Message sent to DLQ: {}", message.getRequestId());
            } catch (Exception nackException) {
                log.error("Error sending NACK: {}", nackException.getMessage());
            }
        }
    }

}
