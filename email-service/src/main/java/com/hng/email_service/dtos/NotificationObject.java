package com.hng.email_service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hng.dtos.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationObject {

    @JsonProperty("notification_type")
    private String notificationType;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("template_code")
    private String templateCode;

    private Map<String, Object> variables;

    @JsonProperty("contact_info")
    private ContactInfo contactInfo;

    private Integer priority;

    private Map<String, Object> metadata;
}
