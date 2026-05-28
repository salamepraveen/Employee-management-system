package com.ems.leave.dto;

import java.time.LocalDate;

import com.ems.leave.entity.LeaveRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDto {
		
	@NotNull(message="Employee Id is required")
	private Long employeeId;
	
	@NotNull(message="Leave type is required")
	private LeaveRequest.LeaveType leaveType;
	
	@NotNull(message="Start date is required")
	private LocalDate startDate;
	
	@NotNull(message="End date is required")
	private LocalDate endDate;
	
	@Size(max=500,message="Reason must not exceed 500 characters")
	private String reason;
}
