package com.hng.user_service.controller;
import com.hng.dtos.ApiResponse;
import com.hng.user_service.dtos.LoginRequest;
import com.hng.user_service.dtos.UserCreationRequest;
import com.hng.user_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hng.user_service.dtos.AuthResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user and returns authentication information",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Registration successful",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody UserCreationRequest req) {

        AuthResponse resp = authService.register(req);

        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .success(true)
                .data(resp)
                .message("Registration successful.")
                .build();

        return ResponseEntity.status(201).body(apiResponse);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticate user and return authentication tokens",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Registration successful",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Registration successful",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
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
