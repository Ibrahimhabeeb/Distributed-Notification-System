package com.hng.notification_dispatcher.controller;

import com.hng.dtos.ApiResponse;
import com.hng.notification_dispatcher.dtos.NotificationResponse;
import com.hng.dtos.NotificationRequest;
import com.hng.notification_dispatcher.service.NotificationPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Notifications", description = "API for sending notifications")
@Slf4j
public class NotificationController {

    private final NotificationPublisher publisher;

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Send a notification",
            description = "Publishes a notification request to the notification system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Notification request payload",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NotificationRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "202",
                            description = "Notification accepted",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @io.swagger.v3.oas.annotations.media.Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Server error",
                            content = @io.swagger.v3.oas.annotations.media.Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> sendNotification(
            @Valid @RequestBody NotificationRequest request,
            @RequestHeader(value = "X-Gateway-Verified", required = false) String gatewayVerified,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {

        log.info("Received notification request - type: {}, request_id: {}, user_id: {}",
                request.getNotificationType(),
                request.getRequestId(),
                userId);

        publisher.publishNotification(request);

        NotificationResponse data = NotificationResponse.builder()
                .status("accepted")
                .requestId(request.getRequestId())
                .build();

        ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                .success(true)
                .data(data)
                .message("Notification queued successfully")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Health check",
            description = "Returns service health status",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Service is up",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}