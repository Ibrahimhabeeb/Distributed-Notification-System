package com.hng.notification_dispatcher.config;


import com.hng.dtos.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "http://localhost:5001"
)
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
    UserDetailsDto getUserDetails(@PathVariable("userId") String userId);
}
