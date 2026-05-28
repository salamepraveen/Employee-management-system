package com.ems.attendance.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ems.common.dto.ApiResponse;

@FeignClient(name="employee-service")
public interface EmployeeClient {
	
		@GetMapping("api/employees/{id}")
		ApiResponse<EmployeeDto> getEmployeeById(@PathVariable("id") Long id);
		
//		@GetMapping("/api/employees/{id}")
//		ResponseEntity<?> checkEmployeeExists(@PathVariable("id") Long id);
}
