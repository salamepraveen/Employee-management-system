package com.ems.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.notification.entity.Notification;
import com.ems.notification.enums.NotificationStatus;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find all notifications for a specific employee
    List<Notification> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    // Find all notifications by status (useful for retrying failed ones)
    List<Notification> findByStatus(NotificationStatus status);

    // Find all pending notifications (for a retry job)
    List<Notification> findByStatusOrderByCreatedAtAsc(NotificationStatus status);
}