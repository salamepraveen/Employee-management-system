package com.ems.leave.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveResponseDto {
			private Long id;
			private Long employeeId;
			private String employeeeName;
			private String department;
			private String leaveType;
			private LocalDate startDate;
			private LocalDate endDate;
			private Integer totalDays;
			private String reason;
			private String status;
			private Long approvedBy;
			private String approverName;
			private String managerComment;
			private LocalDateTime createdAt;
			private LocalDateTime updatedAt;
}
