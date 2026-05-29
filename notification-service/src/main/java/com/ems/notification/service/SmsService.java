package com.ems.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    public void sendSms(String recipient, String message) {
        log.info("Sending SMS to: {}, Message: {}", recipient, message);
        // Simulation for now
        log.info("SMS sent successfully to: {}", recipient);
    }
}
