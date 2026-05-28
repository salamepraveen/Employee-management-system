package com.ems.payroll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRequestDto {
	
	@NotNull(message="Employee ID is required")
	private Long employeeId;
	
	@NotNull(message="Month is required (1-12")
	@Min(value=1,message="Month must be between 1 and 12")
	@Max(value=12,message="Month must be between 1 and 12")
	private Integer month;
	
	@NotNull(message = "Year is required")
    @Min(value = 2020, message = "Invalid year")
    @Max(value = 2100, message = "Invalid year")
	private Integer year;
	
	private Double bonus;
	private Double deductions;
	
	
}
