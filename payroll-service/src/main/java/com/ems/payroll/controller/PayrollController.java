package com.ems.payroll.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.common.dto.ApiResponse;
import com.ems.payroll.dto.PayrollRequestDto;
import com.ems.payroll.dto.PayrollResponseDto;
import com.ems.payroll.service.PayrollService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/payroll")
public class PayrollController {
		
	private final PayrollService payrollService;
	
	
	@Autowired
	public PayrollController(PayrollService payrollService) {
		this.payrollService=payrollService;
	}
	
	@PostMapping("/generate")
	public ResponseEntity<ApiResponse<PayrollResponseDto>> generatePayroll(@Valid @RequestBody PayrollRequestDto dto){
		
		PayrollResponseDto response=payrollService.generatePayroll(dto);
		
		return new ResponseEntity<>(ApiResponse.success("Payroll generated successfully", response),HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PayrollResponseDto>> getPayroll(@PathVariable Long id){
		PayrollResponseDto response=payrollService.getPayroll(id);
		
		return ResponseEntity.ok(ApiResponse.success("Payroll retreived",response));
	}
	
	 @GetMapping("/employee/{employeeId}")
	    public ResponseEntity<ApiResponse<List<PayrollResponseDto>>> getEmployeePayrollHistory(
	            @PathVariable Long employeeId) {

	        List<PayrollResponseDto> history =
	                payrollService.getEmployeePayrollHistory(employeeId);

	        return ResponseEntity.ok(
	                ApiResponse.success("Payroll history retrieved", history));
	    }
	 
	 @PutMapping("/{id}/approve")
	    public ResponseEntity<ApiResponse<PayrollResponseDto>> approvePayroll(
	            @PathVariable Long id,
	            @RequestParam Long managerId) {

	        PayrollResponseDto response = payrollService.approvePayroll(id, managerId);

	        return ResponseEntity.ok(
	                ApiResponse.success("Payroll approved successfully", response));
	    }
	
}
