package com.ems.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ems.attendance.entity.Attendance.AttendanceBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponseDto {
		
	private Long id;
	private Long employeeId;
	private String employeeName;//fetchd from employee service
	private String department; // fetcjed from employee service
	private LocalDate date;
	private LocalDateTime checkIn;
	private LocalDateTime checkOut;
	private String status;
	private Double hoursWorked;
	private LocalDateTime createdAt;
	
}
