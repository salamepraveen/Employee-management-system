package com.ems.attendance.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInRequest {
		
	@NotNull(message="Employee ID is required")
	private Long employeeId;
	
	private LocalDateTime checkIn;
}
