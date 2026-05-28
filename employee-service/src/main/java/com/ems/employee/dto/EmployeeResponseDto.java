package com.ems.employee.dto;

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
public class EmployeeResponseDto {
			
	private Long id;
	private String firstName;
	private String lastName;
	
	private String fullname;
	
	private String email;
	private String phone;
	private String department;
	private String position;
	private LocalDate hireDate;
	private Double salary;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
