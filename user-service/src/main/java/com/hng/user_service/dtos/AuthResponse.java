package com.hng.user_service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;


@Value
public class AuthResponse {
    @JsonProperty("token")
    String jwtToken;

    String userid;
}
