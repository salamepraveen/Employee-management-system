 package com.ems.leave.service;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.common.dto.ApiResponse;
import com.ems.common.exception.BusinessException;
import com.ems.common.exception.ResourceNotFoundException;
import com.ems.leave.cleint.EmployeeClient;
import com.ems.leave.cleint.EmployeeDto;
import com.ems.leave.dto.LeaveApprovalDto;
import com.ems.leave.dto.LeaveRequestDto;
import com.ems.leave.dto.LeaveResponseDto;
import com.ems.leave.entity.LeaveRequest;
import com.ems.leave.repository.LeaveRequestRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveService {
	
	private final LeaveRequestRepository leaveRepository;
	private final EmployeeClient employeeClient;
	
	@Autowired
	public LeaveService(LeaveRequestRepository leaveRepository,EmployeeClient employeeClient) {
		this.leaveRepository=leaveRepository;
		this.employeeClient=employeeClient;
	}
	
	public LeaveResponseDto requestLeave(LeaveRequestDto dto) {
		EmployeeDto employee=employeeClient.getEmployeeById(dto.getEmployeeId()).getData();
	
		if(dto.getEndDate().isBefore(dto.getStartDate())) {
			throw new BusinessException("End date cannot be before start date",400);
		}
		
		if(dto.getStartDate().isBefore(LocalDate.now())) {
			throw new BusinessException("Cannot request leave for past dates",400);
		}
		
		long days=ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
		if(days>30) {
			throw new BusinessException("Leave duration cannot exceed 30 days",400);
		}
		
		List<LeaveRequest> overlapping=leaveRepository.findOverlappingApprovedLeaves(dto.getEmployeeId(),dto.getStartDate(),dto.getEndDate());
		
		if(!overlapping.isEmpty()) {
			throw new BusinessException("You already have approved leave on some of these daates. "+"Please choose different dates. ",409);
		}
		
		LeaveRequest leaveRequest=LeaveRequest.builder()
				.employeeId(dto.getEmployeeId())
				.leaveType(dto.getLeaveType())
				.startDate(dto.getStartDate())
				.endDate(dto.getEndDate())
				.totalDays((int) days)
				.reason(dto.getReason())
				.status(LeaveRequest.LeaveStatus.PENDING)
				.build();
		leaveRequest = leaveRepository.save(leaveRequest);
		return buildLeaveResponse(leaveRequest,employee,null);
	}
	
	
	
	public LeaveResponseDto approveOrReject(Long leaveId,Long managerId,LeaveApprovalDto dto) {
		LeaveRequest leave=leaveRepository.findById(leaveId).orElseThrow(()-> new ResourceNotFoundException("Leave request","id",leaveId));
		
		if(leave.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
			throw new BusinessException("Leave request is already"+leave.getStatus()+" .Only PENDING requests can be approved or rejected.",409);
		}
		if(dto.getAction()==LeaveApprovalDto.Action.REJECT &&(dto.getManagerComment()==null || dto.getManagerComment().trim().isEmpty())) {
			throw new BusinessException("Manager comment is required  when rejecting a leave request",400);
			
			
		}
		
		if(dto.getAction()==LeaveApprovalDto.Action.APPROVE) {
			leave.setStatus(LeaveRequest.LeaveStatus.APPROVED);
		}else {
			leave.setStatus(LeaveRequest.LeaveStatus.REJECTED);
		}
		
		leave.setApprovedBy(managerId);
		leave.setManagerComment(dto.getManagerComment());
		leave=leaveRepository.save(leave);
		
		EmployeeDto employee=getEmployeeInfo(leave.getEmployeeId());
		EmployeeDto manager=getEmployeeInfo(managerId);
		
		return buildLeaveResponse(leave,employee,manager);
	}
	
	public LeaveResponseDto cancelLeave(Long leaveId,Long employeeId) {
		LeaveRequest leave=leaveRepository.findById(leaveId).orElseThrow(()-> new ResourceNotFoundException("Leave request", "id",leaveId));
		
		if(!leave.getEmployeeId().equals(employeeId)) {
			throw new BusinessException("You can only cancel you own leave requests",403);
		}
		
		if(leave.getStatus()!=LeaveRequest.LeaveStatus.PENDING) {
			throw new BusinessException("Only PENDING leave requests can be cancelled. "+"This request is already "+leave.getStatus()+".",409);
		}
		
		leave.setStatus(LeaveRequest.LeaveStatus.CANCELLED);
		leave=leaveRepository.save(leave);
		
		EmployeeDto employee=employeeClient.getEmployeeById(leave.getEmployeeId()).getData();
		return buildLeaveResponse(leave,employee,null);
		
	}
	
	@Transactional(readOnly=true)
	public List<LeaveResponseDto> getEmployeeLeaveHistory(Long employeeId){
		List<LeaveRequest> leaves=leaveRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
		
		return leaves.stream().map(lr->{
			EmployeeDto emp=getEmployeeInfo(lr.getEmployeeId());
			EmployeeDto approver=lr.getApprovedBy()!=null ? getEmployeeInfo(lr.getApprovedBy()) : null;
			return buildLeaveResponse(lr,emp,approver);
		}).toList();
	}
	
	 @Transactional(readOnly = true)
	public List<LeaveResponseDto> getPendingRequests(){
		List<LeaveRequest> pending=leaveRepository.findByStatusOrderByCreatedAtDesc(LeaveRequest.LeaveStatus.PENDING);
		
		return pending.stream().map(lr->{
								EmployeeDto emp=getEmployeeInfo(lr.getEmployeeId()); 
								return buildLeaveResponse(lr,emp,null);
		}
				).toList();
				
				
	}
	
	
	private EmployeeDto getEmployeeInfo(Long employeeId) {
		try {
			ApiResponse<EmployeeDto> response = employeeClient.getEmployeeById(employeeId);
			if (response != null && response.getData() != null) {
				return response.getData();
			}
		} catch (Exception e) {
			System.out.println("Could not fetch employee info for ID: " + employeeId);
		}
		return null;
	}
	
	private LeaveResponseDto buildLeaveResponse(LeaveRequest leave, EmployeeDto employee, EmployeeDto approver) {
		return LeaveResponseDto.builder()
				.id(leave.getId())
				.employeeId(leave.getEmployeeId())
				.employeeeName(employee != null ? employee.getFirstName() + " " + employee.getLastName() : "Unknown")
				.department(employee != null ? employee.getDepartment() : "Unknown")
				.leaveType(leave.getLeaveType().name())
				.startDate(leave.getStartDate())
				.endDate(leave.getEndDate())
				.totalDays(leave.getTotalDays())
				.reason(leave.getReason())
				.status(leave.getStatus().name())
				.approvedBy(leave.getApprovedBy())
				.approverName(approver != null ? approver.getFirstName() + " " + approver.getLastName() : null)
				.managerComment(leave.getManagerComment())
				.createdAt(leave.getCreatedAt())
				.updatedAt(leave.getUpdatedAt())
				.build();
	}
}
