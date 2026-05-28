package com.ems.payroll.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ems.common.dto.ApiResponse;


@FeignClient(name="leave-service")
public interface LeaveClient {
	
	@GetMapping("api/leaves/employee/{employeeId")
	ApiResponse<LeaveDto> getEmployeeLeaves(@PathVariable("employeeId") Long employeeId);
}
