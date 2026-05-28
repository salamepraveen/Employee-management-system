 package com.ems.employee.dto;

import java.time.LocalDate;

import com.ems.employee.entity.Employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDto {
	
	@NotBlank(message="First name is required")
	@Size(min=2, max=50,message="First name must be between 2 and 50 characters")
	private String firstName;
	
	@NotBlank(message="Last name is required")
	@Size(min=2, max=50, message="Last name must be between 2 and 50 characters")
	private String lastName;
	
	@NotBlank(message="Email is required")
	@Email(message="Inavlid email format")
	private String email;
	
	@Pattern(regexp="^[0-9+\\-()\\s]*$", message="Inavlid number format")
	private String phone;
	
	@NotNull(message="Department is required")
	private Employee.Department department;
	
	@NotBlank(message="Position is required")
	@Size(min=2,max=50,message="Position  must be between 2 and 50 characters")
	private String position;
	
	@NotNull(message="Hire date is required")
	@PastOrPresent(message="Hire date cannot be in the future")
	private LocalDate hireDate;
	
	@NotNull(message="Salary is required")
	@Positive(message="Salary must be a positive number")
	private Double salary;
	
	
	
}
