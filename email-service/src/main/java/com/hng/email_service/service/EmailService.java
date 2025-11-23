package com.hng.email_service.service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hng.email_service.dtos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hng.dtos.UserData;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final TemplateService templateService;
     private  final EmailSenderService emailSenderService;



    @Value("${mailersend.api.from-email}")
    private String fromEmail;

//    @Value("${mailersend.api.name}"))
//    private String toEmail;

    public void processNotification(NotificationObject message) {
        log.info("Processing notification for user: {}, template: {}",
                message.getUserId(), message.getTemplateCode());

        try {
         
            TemplateResponse templateResponse = templateService.getLatestTemplate(
                    message.getTemplateCode(), "en");

            if (templateResponse == null || !templateResponse.getSuccess()) {
                throw new RuntimeException("Failed to fetch template");
            }

            TemplateData template = templateResponse.getData();


            String subject = replaceVariables(template.getSubject(), message.getVariables());
            String body = replaceVariables(template.getBody(), message.getVariables());


            String name = message.getVariables().getOrDefault("name", "User") != null
                    ? message.getVariables().getOrDefault("name", "User").toString()
                    : "User";

            MailerSendRequest emailRequest = createEmailRequest(
                    message.getContactInfo().getEmail(),
                    name,
                    subject,
                    body,
                    message.getVariables()
            );


            emailSenderService.sendEmail(emailRequest);

            log.info("Email processed successfully for request: {}", message.getRequestId());

        } catch (Exception e) {
            log.error("Error processing notification: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process notification", e);
        }
    }


//    private String replaceVariables(String template, Map<String, Object> variables) {
//        String result = template;
//        Pattern pattern = Pattern.compile("\\{\\{(\\w+)\\}\\}");
//        Matcher matcher = pattern.matcher(template);
//
//        while (matcher.find()) {
//            String variable = matcher.group(1);
//            String value = variables.getOrDefault(variable, "").toString();
//
//            result = result.replace("{{" + variable + "}}", value);
//        }
//
//        return result;
//    }

    private String replaceVariables(String template, Map<String, Object> variables) {
        if (template == null || variables == null) {
            return template;
        }

        String result = template;
        Pattern pattern = Pattern.compile("\\{\\{(\\w+)\\}\\}");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String variable = matcher.group(1);
            Object valueObj = variables.get(variable);
            String value = valueObj != null ? valueObj.toString() : "";

            result = result.replace("{{" + variable + "}}", value);
        }

        return result;
    }

//    private MailerSendRequest createEmailRequest(String toEmail, String toName,
//                                                 String subject, String body) {
//        MailerSendRequest request = new MailerSendRequest();
//
//        MailerSendFrom from = new MailerSendFrom();
//        from.setEmail(fromEmail);
//
//        request.setFrom(from);
//
//
//        MailerSendRecipient recipient = new MailerSendRecipient(toEmail, toName);
//        request.setTo(new MailerSendRecipient[]{recipient});
//
//        request.setSubject(subject);
//        request.setHtml(body);
//        request.setText(body.replaceAll("<[^>]*>", "")); // Strip HTML for text version
//
//        return request;
//    }



    private MailerSendRequest createEmailRequest(String toEmail, String toName,
                                                 String subject, String body,
                                                 Map<String, Object> variables) {
        MailerSendRequest request = new MailerSendRequest();

        MailerSendFrom from = new MailerSendFrom();
        from.setEmail("notifications@test-eqvygm05ppjl0p7w.mlsender.net");
        from.setName("Notifications");
        request.setFrom(from);

        MailerSendRecipient recipient = new MailerSendRecipient("ibrahim.habeeb2004@gmail.com", toName);
        request.setTo(Collections.singletonList(recipient));

        request.setSubject(subject);
        request.setHtml(body);
        request.setText(body.replaceAll("<[^>]*>", ""));

        // Convert variables to Map<String, String> for personalization
//        Map<String, String> dataMap = new HashMap<>();
//        if (variables != null) {
//            variables.forEach((k, v) -> {
//                if (v instanceof Map) {
//                    ((Map<?, ?>) v).forEach((subKey, subVal) ->
//                            dataMap.put(subKey.toString(), subVal != null ? subVal.toString() : "")
//                    );
//                } else {
//                    dataMap.put(k, v != null ? v.toString() : "");
//                }
//            });
//        }
//        dataMap.put("email", toEmail);

//        MailerSendPersonalization personalization = new MailerSendPersonalization();
//        personalization.setEmail(toEmail);
//        personalization.setData(dataMap);
//
//        request.setPersonalization(Collections.singletonList(personalization));

        return request;
    }


}
