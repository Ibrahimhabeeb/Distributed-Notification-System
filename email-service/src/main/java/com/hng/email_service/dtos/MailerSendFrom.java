package com.hng.email_service.dtos;

public class MailerSendFrom {
    private String email;
    private String name;

    // Constructors
    public MailerSendFrom() {}

    public MailerSendFrom(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}