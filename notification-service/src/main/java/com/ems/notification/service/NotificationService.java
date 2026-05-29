package com.ems.notification.service;

import com.ems.common.exception.ResourceNotFoundException;
import com.ems.notification.dto.NotificationResponseDto;
import com.ems.notification.dto.SendNotificationRequest;
import com.ems.notification.entity.Notification;
import com.ems.notification.enums.NotificationStatus;
import com.ems.notification.enums.NotificationType;
import com.ems.notification.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public NotificationResponseDto sendNotification(SendNotificationRequest request) {
        log.info("Creating notification for employee: {}", request.getEmployeeId());

        Notification notification = Notification.builder()
                .employeeId(request.getEmployeeId())
                .type(request.getType())
                .subject(request.getSubject())
                .message(request.getMessage())
                .recipient(request.getRecipient())
                .status(NotificationStatus.PENDING)
                .build();

        notification = notificationRepository.save(notification);
        log.info("Notification saved to DB with ID: {} and status: PENDING", notification.getId());

        try {
            dispatchNotification(notification);

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
            log.info("Notification ID: {} status updated to SENT", notification.getId());

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            notificationRepository.save(notification);
            log.error("Notification ID: {} status updated to FAILED. Reason: {}", 
                    notification.getId(), e.getMessage());
        }

        return toDto(notification);
    }

    private void dispatchNotification(Notification notification) {
        if (notification.getType() == NotificationType.EMAIL) {
            emailService.sendEmail(
                    notification.getRecipient(),
                    notification.getSubject(),
                    notification.getMessage()
            );
        } else if (notification.getType() == NotificationType.SMS) {
            smsService.sendSms(
                    notification.getRecipient(),
                    notification.getMessage()
            );
        } else {
            log.warn("Unsupported notification type: {} for notification ID: {}", 
                    notification.getType(), notification.getId());
            throw new UnsupportedOperationException(
                    "Notification type not supported: " + notification.getType());
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getEmployeeNotifications(Long employeeId) {
        log.info("Fetching notifications for employee: {}", employeeId);

        List<Notification> notifications = notificationRepository
                .findByEmployeeIdOrderByCreatedAtDesc(employeeId);

        return notifications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationResponseDto getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
        return toDto(notification);
    }

    public int retryFailedNotifications() {
        log.info("Retrying failed notifications...");

        List<Notification> failedNotifications = 
                notificationRepository.findByStatus(NotificationStatus.FAILED);

        int retryCount = 0;
        for (Notification notification : failedNotifications) {
            try {
                notification.setStatus(NotificationStatus.PENDING);
                notification.setErrorMessage(null);
                notificationRepository.save(notification);

                dispatchNotification(notification);

                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
                notificationRepository.save(notification);
                retryCount++;

                log.info("Retry successful for notification ID: {}", notification.getId());

            } catch (Exception e) {
                notification.setStatus(NotificationStatus.FAILED);
                notification.setErrorMessage("Retry failed: " + e.getMessage());
                notificationRepository.save(notification);

                log.error("Retry still failing for notification ID: {}. Error: {}", 
                        notification.getId(), e.getMessage());
            }
        }

        log.info("Retried {} notifications. {} successful.", 
                failedNotifications.size(), retryCount);
        return retryCount;
    }

    private NotificationResponseDto toDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setEmployeeId(notification.getEmployeeId());
        dto.setType(notification.getType());
        dto.setSubject(notification.getSubject());
        dto.setMessage(notification.getMessage());
        dto.setRecipient(notification.getRecipient());
        dto.setStatus(notification.getStatus());
        dto.setSentAt(notification.getSentAt());
        dto.setErrorMessage(notification.getErrorMessage());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());
        return dto;
    }
}