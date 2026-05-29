package com.ems.payroll.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
		private Long id;
		private String firstName;
		private String lastName;
		private String department;
		private String position;
		private Double salary; // monthly base salary
		private String status;
}
