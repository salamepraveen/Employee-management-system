package com.ems.payroll.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ems.common.dto.ApiResponse;
import com.ems.common.dto.PageResponse;

@FeignClient(name="attendance-service")
public interface AttendanceClient {
	@GetMapping("/api/attendance/employee/{employeeId}")
	ApiResponse<PageResponse<AttandanceDto>> getEmployeeAttendance(
			@PathVariable("employeeId") Long employeeId,
			@RequestParam("startDate") String startDate, 
			@RequestParam("endDate") String endDate);
}
