package com.ems.notification.enums;

public enum NotificationStatus {
    PENDING,    // Created but not yet sent
    SENT,       // Successfully delivered
    FAILED      // Delivery failed — check errorMessage field
}
