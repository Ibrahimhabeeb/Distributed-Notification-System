package com.hng.email_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailerSendPersonalization {
    private String email;
    private Map<String, String> data;
}
