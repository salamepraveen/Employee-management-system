package com.ems.payroll.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ems.common.dto.ApiResponse;

@FeignClient(name="employee-service")
public interface EmployeeClient {
		
	@GetMapping("/api/employees/{id}")
	ApiResponse<EmployeeDto> getEmployeeId(@PathVariable("id") Long id);
}
