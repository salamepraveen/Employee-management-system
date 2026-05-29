package com.ems.payroll.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponseDto {
	
	private Long id;
	private Long employeeId;
	private String employeeName;
	private String department;
	private String position;
	private Integer month;
	private Integer year;
	private String paymentMonth;
	
	private Double basicSalary;
	private Double overtime;
	private Double bonus;
	private Double grossSalary;
	
	
	private Double leaveDeduction;
	private Double tax;
	private Double deductions;
	private Double totalDeductions;
	
	private Double netSalary;
	
	private Integer daysPresent;
	private Integer daysAbsent;
	private Integer workingDays;
	private Integer leaveDays;
	
	private String status;
	private LocalDateTime createdAt;
	
}
