package com.ems.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.common.dto.ApiResponse;
import com.ems.common.dto.PageResponse;
import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.entity.Employee;
import com.ems.employee.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService=employeeService;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(@Valid @RequestBody EmployeeRequestDto dto){
		EmployeeResponseDto createdEmployee=employeeService.createEmployee(dto);
		
		ApiResponse<EmployeeResponseDto> response=ApiResponse.success("Employee created successfully", createdEmployee);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeById(@PathVariable Long id){
		EmployeeResponseDto employee=employeeService.getEmployeeById(id);
		
		return ResponseEntity.ok(ApiResponse.success("Employee Found",employee));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<EmployeeResponseDto>>> getAllEmployees(
			@RequestParam(defaultValue="0") int page,
			@RequestParam(defaultValue="20") int size,
			@RequestParam(defaultValue="id") String sortBy,
			@RequestParam(defaultValue="asc") String sortDir){
		
		PageResponse<EmployeeResponseDto> employees=employeeService.getAllEmployees(page, size, sortBy, sortDir);
		
		return ResponseEntity.ok(
				ApiResponse.success("Employees retrieved successfully", employees));
		
	}
	
	public ResponseEntity<ApiResponse<PageResponse<EmployeeResponseDto>>> searchEmployees(
			@RequestParam(required=false) String name,
			@RequestParam(required=false) Employee.Department department,
			@RequestParam(required=false) Employee.EmployeeStatus status,
			@RequestParam(required=false) Double minSalary,
			@RequestParam(required=false) Double maxSalary,
			@RequestParam(defaultValue="0") int page,
			@RequestParam(defaultValue="20") int size,
			@RequestParam(defaultValue="id") String sortBy,
			@RequestParam(defaultValue="asc") String sortDir){
		
		
		PageResponse<EmployeeResponseDto> employees=employeeService.searchEmployees
													(name, department, status, minSalary, maxSalary, page, size, sortBy, sortDir);
		return ResponseEntity.ok(ApiResponse.success("Search completed successfull", employees));
	}
	
	@PutMapping
	public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateEmployee(
			@PathVariable Long id,
			@Valid @RequestBody EmployeeRequestDto dto
			
			)	{
		EmployeeResponseDto updatedEmployee=employeeService.update(id,dto);
		
		return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updatedEmployee));
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteEmployee(
			@PathVariable Long id
			){
		employeeService.deleteEmployee(id);
		
		return ResponseEntity.ok(ApiResponse.success("Employee deleted Successfully"));
	}
	
	
	@GetMapping("/department/{department}")
	public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getEmployeeByDepartment(
			@PathVariable Employee.Department department
			){
		List<EmployeeResponseDto> employees=employeeService.getEmployeeByDepartment(department);
		
		return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
	}
	

}
