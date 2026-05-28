package com.ems.attendance.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
		
	private Long id;
	private String fristName;
	private String lastName;
	private String department;
}
