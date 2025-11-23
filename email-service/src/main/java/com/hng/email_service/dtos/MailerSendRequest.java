package com.hng.email_service.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//public class MailerSendRequest {
//    private MailerSendFrom from;
//    private MailerSendRecipient[] to;
//    private String subject;
//    private String text;
//    private String html;
//}


public class MailerSendRequest {
    private MailerSendFrom from;
    private List<MailerSendRecipient> to;
    private String subject;
    private String text;
    private String html;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MailerSendPersonalization> personalization;
}

