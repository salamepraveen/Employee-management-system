package com.ems.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.ems")
@EnableAsync       // Enables @Async on EmailService.sendEmail()
@EnableScheduling // Enables @Scheduled for retry job
public class NotificationServiceApplication {
		public static void main (String args[]) {
			SpringApplication.run(NotificationServiceApplication.class, args);
		}
}
