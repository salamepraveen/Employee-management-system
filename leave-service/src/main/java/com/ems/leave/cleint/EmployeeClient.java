package com.ems.leave.cleint;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ems.common.dto.ApiResponse;


@FeignClient(name="employee-service")
public interface EmployeeClient {
		
	
	@GetMapping("/api/employee/{id}")
	ApiResponse<EmployeeDto> getEmployeeById(@PathVariable("id") Long id);
}
