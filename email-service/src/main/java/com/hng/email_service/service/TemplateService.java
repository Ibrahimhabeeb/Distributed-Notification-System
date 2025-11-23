package com.hng.email_service.service;

import com.hng.email_service.dtos.TemplateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@Slf4j

public class TemplateService {

    private final WebClient templateServiceWebClient;


    public TemplateService(
            @Qualifier("templateServiceWebClient") WebClient templateServiceWebClient
    ) {
        this.templateServiceWebClient = templateServiceWebClient;
    }
    public TemplateResponse getLatestTemplate(String templateKey, String language) {
        log.info("Fetching template: {} for language: {}", templateKey, language);

        try {
            return templateServiceWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/templates/get_latest")
                            .queryParam("template_key", templateKey)
                            .queryParam("language", language)
                            .build())
                    .retrieve()
                    .bodyToMono(TemplateResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching template: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch template", e);
        }
    }
}

