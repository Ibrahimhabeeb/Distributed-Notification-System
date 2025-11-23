package com.hng.email_service.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TemplateData {
    @JsonProperty("template_key")
    private String templateKey;

    private String language;

    @JsonProperty("version_number")
    private Integer versionNumber;

    private String subject;
    private String body;
    private String[] variables;
}
