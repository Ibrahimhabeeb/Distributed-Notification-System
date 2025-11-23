package com.hng.email_service.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hng.email_service.config.WebClientConfig;
import com.hng.email_service.dtos.MailerSendRequest;
import com.hng.email_service.dtos.Recipient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import  com.hng.dtos.NotificationRequest;
@Service
@Slf4j
public class EmailSenderService {

    private final WebClient mailerSendWebClient;

    public EmailSenderService(
            @Qualifier("mailerSendWebClient") WebClient mailerSendWebClient
    ) {
        this.mailerSendWebClient = mailerSendWebClient;
    }

    @Value("${mailersend.api.key}")
    private String mailersendKey;



    public void sendEmail(MailerSendRequest request) {


        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            log.info("Sending email payload:\n{}", json);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.error("Failed to serialize email request", e);
        }






        try {
            String response = mailerSendWebClient.post()
                    .uri("/email")
                    .header("Authorization", "Bearer " + mailersendKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Email sent successfully. Response: {}", response);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }






}
