package com.hng.email_service.dtos;
import lombok.Data;

@Data
public class TemplateResponse {
    private Boolean success;
    private TemplateData data;
    private String error;
    private String message;
}