package com.hng.notification_dispatcher.controller;

import com.hng.dtos.ApiResponse;
import com.hng.notification_dispatcher.dtos.NotificationResponse;
import com.hng.notification_dispatcher.model.NotificationRequest;
import com.hng.notification_dispatcher.service.NotificationPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationPublisher publisher;


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

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

}
