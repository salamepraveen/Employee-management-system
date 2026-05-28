package com.ems.leave.controller;

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
import com.ems.leave.dto.LeaveApprovalDto;
import com.ems.leave.dto.LeaveRequestDto;
import com.ems.leave.dto.LeaveResponseDto;
import com.ems.leave.service.LeaveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {
		
	private final LeaveService leaveService;
	
	@Autowired
	public LeaveController(LeaveService leaveService) {
		this.leaveService=leaveService;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<LeaveResponseDto>> requestLeave(@Valid @RequestBody LeaveRequestDto dto){
		
		LeaveResponseDto response=leaveService.requestLeave(dto);
		
		return new ResponseEntity<>(ApiResponse.success("Leave request submitted successfully",response),HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<ApiResponse<LeaveResponseDto>> approveOrReject(
			@PathVariable Long id, @RequestParam Long managerId, @Valid @RequestBody LeaveApprovalDto dto){
		LeaveResponseDto response=leaveService.approveOrReject(id,managerId,dto);
		
		return ResponseEntity.ok(ApiResponse.success("Leave request "+dto.getAction().name().toLowerCase()+" successfully",response));
	}
	
	
	@PutMapping("/{id}/cancel")
	public ResponseEntity<ApiResponse<LeaveResponseDto>> cancelLeave(@PathVariable Long id, @RequestParam long employeeId){
		LeaveResponseDto response=leaveService.cancelLeave(id, employeeId);
		
		return ResponseEntity.ok(ApiResponse.success("Leave request cancelled successfully", response));
	}
	
	@GetMapping("/employee/{employeeid}")
	public ResponseEntity<ApiResponse<List<LeaveResponseDto>>> getEmployeeLeaves(@PathVariable Long employeeId){
		List<LeaveResponseDto> leaves=leaveService.getEmployeeLeaveHistory(employeeId);
		
		return ResponseEntity.ok(ApiResponse.success("Leave history retrieved",leaves));
	}
	
	
	@GetMapping("/pending")
	public ResponseEntity<ApiResponse<List<LeaveResponseDto>>> getPendingRequests(){
		List<LeaveResponseDto> pending=leaveService.getPendingRequests();
		
		
		return ResponseEntity.ok(ApiResponse.success("Pending requests retreived", pending));
	}
	
}
