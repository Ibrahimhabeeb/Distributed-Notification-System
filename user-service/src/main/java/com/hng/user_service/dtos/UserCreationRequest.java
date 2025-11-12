package com.hng.user_service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class UserCreationRequest {

    private String name;

    private String email;

    @JsonProperty("push_token")
    private String pushToken;


    private Map<String, Object> preferences;

    private String password;

}


