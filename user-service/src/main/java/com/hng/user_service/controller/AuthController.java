package com.hng.user_service.controller;
import com.hng.dtos.ApiResponse;
import com.hng.user_service.dtos.LoginRequest;
import com.hng.user_service.dtos.UserCreationRequest;
import com.hng.user_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hng.user_service.dtos.AuthResponse;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody UserCreationRequest req) {

        AuthResponse resp = authService.register(req);

        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .success(true)
                .data(resp)
                .message("Registration successful.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest req) {

        AuthResponse resp = authService.login(req);

        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .success(true)
                .data(resp)
                .message("Login successful.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }





}
