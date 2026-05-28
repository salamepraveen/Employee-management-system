package com.ems.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages="com.ems")
public class AttendanceServiceApplication {
	
		public static void main(String args[]) {
			SpringApplication.run(AttendanceServiceApplication.class,args);
		}

}
