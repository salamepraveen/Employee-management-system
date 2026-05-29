package com.ems.notification.controller;

import com.ems.common.dto.ApiResponse;
import com.ems.notification.dto.NotificationResponseDto;
import com.ems.notification.dto.SendNotificationRequest;
import com.ems.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * POST /api/notifications
     * Send a new notification (email or SMS)
     * 
     * Called BY other services via Feign:
     *   - leave-service → "Leave approved/rejected"
     *   - payroll-service → "Payslip generated"
     *   - attendance-service → "Late arrival warning"
     */
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDto>> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        
        log.info("Received request to send {} notification to employee: {}", 
                request.getType(), request.getEmployeeId());

        NotificationResponseDto response = notificationService.sendNotification(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<NotificationResponseDto>builder()
                        .timestamp(java.time.LocalDateTime.now())
                        .status(201)
                        .message("Notification sent successfully")
                        .data(response)
                        .build());
    }

    /**
     * GET /api/notifications/employee/{employeeId}
     * Get all notifications for a specific employee
     * 
     * Used by the frontend when an employee opens their "Notifications" page
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> 
            getEmployeeNotifications(@PathVariable Long employeeId) {
        
        log.info("Fetching notifications for employee: {}", employeeId);

        List<NotificationResponseDto> notifications = 
                notificationService.getEmployeeNotifications(employeeId);

        return ResponseEntity.ok(
                ApiResponse.success("Notifications fetched successfully", notifications));
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> 
            getNotificationById(@PathVariable Long id) {
        
        log.info("Fetching notification with ID: {}", id);

        NotificationResponseDto notification = notificationService.getNotificationById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Notification fetched successfully", notification));
    }

  
    @PostMapping("/retry")
    public ResponseEntity<ApiResponse<Integer>> retryFailedNotifications() {
        
        log.info("Manual retry triggered for failed notifications");

        int retryCount = notificationService.retryFailedNotifications();

        return ResponseEntity.ok(
                ApiResponse.success("Retry completed. " + retryCount + " notifications sent.", retryCount));
    }
}