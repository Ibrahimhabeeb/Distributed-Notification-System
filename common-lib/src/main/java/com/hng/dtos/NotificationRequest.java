package com.hng.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;
@Data
public class NotificationRequest {
    @NotNull
    @JsonProperty("notification_type")
    private NotificationType notificationType;

    @NotNull
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("request_id")
    private String requestId;

    @NotBlank
    @JsonProperty("template_code")
    private String templateCode;

    @NotNull
    @Valid
    private UserData variables;


    @JsonProperty("contact_info")
    private ContactInfo contactInfo;
    private Integer priority;

    private Map<String, Object> metadata;


    public NotificationRequest() {}

    // Getters and Setters
    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public UUID getUserId() {
        return userId;
    }


    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public UserData getVariables() {
        return variables;
    }

    public void setVariables(UserData variables) {
        this.variables = variables;
    }




    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}