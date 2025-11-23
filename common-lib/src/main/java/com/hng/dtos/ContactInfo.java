package com.hng.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactInfo {
    private String email;
    private String phone;
    private String pushToken;
}