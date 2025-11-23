package com.hng.email_service.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailerSendRecipient {
    private String email;
    private String name;

    public MailerSendRecipient(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
