package com.hng.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDetailsDto {
    private String email;
    private String name;

    @JsonProperty("push_token")
    private String pushToken;

    public UserDetailsDto(
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("push_token") String pushToken
    ) {
        this.email = email;
        this.name = name;
        this.pushToken = pushToken;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPushToken() { return pushToken; }
}
