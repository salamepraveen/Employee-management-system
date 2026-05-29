package com.ems.notification.dto;

import java.time.LocalDateTime;

import com.ems.notification.enums.NotificationStatus;
import com.ems.notification.enums.NotificationType;

import lombok.Data;
@Data
public class NotificationResponseDto {

    private Long id;
    private Long employeeId;
    private NotificationType type;
    private String subject;
    private String message;
    private String recipient;
    private NotificationStatus status;
    private LocalDateTime sentAt;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}