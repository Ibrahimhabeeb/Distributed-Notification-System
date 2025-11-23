package com.hng.email_service.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${mailersend.api.key}")
    private String mailersendApiKey;

    @Value("${mailersend.api.base-url}")
    private String mailersendBaseUrl;

    @Value("${template.service.url}")
    private  String templateUrl;

    @Bean
    public WebClient mailerSendWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(mailersendBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + mailersendApiKey) // <-- Authentication header
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient templateServiceWebClient() {
        return WebClient.builder()
                .baseUrl(templateUrl)
                .build();
    }
//
//    @Bean
//    public WebClient mailerSendWebClient() {
//        return WebClient.builder()
//                .baseUrl(mailersendBaseUrl)
//                .defaultHeader("Authorization", mailersendApiKey)
//                .defaultHeader("Content-Type", "application/json")
//                .build();
//    }




}