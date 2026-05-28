package com.ems.attendance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.attendance.dto.AttendanceResponseDto;
import com.ems.attendance.dto.CheckInRequest;
import com.ems.attendance.dto.CheckOutRequest;
import com.ems.attendance.service.AttendanceService;
import com.ems.common.dto.ApiResponse;
import com.ems.common.dto.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
		
	private final AttendanceService attendanceService;
	
	@Autowired
	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService=attendanceService;
		
	}
	
	@PostMapping("/check-in")
	public ResponseEntity<ApiResponse<AttendanceResponseDto>> checkIn(@Valid @RequestBody CheckInRequest request){
		
		AttendanceResponseDto response=attendanceService.checkIn(request);
		
		return new ResponseEntity<>(ApiResponse.success("Check-in successful",response),HttpStatus.CREATED);
	}
	
	@PostMapping("/check-out")
	public ResponseEntity<ApiResponse<AttendanceResponseDto>> checkOut(@Valid @RequestBody CheckOutRequest request){
		
		AttendanceResponseDto response=attendanceService.checkOut(request);
		return ResponseEntity.ok(ApiResponse.success("Check out successfull",response));

	}
	
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<ApiResponse<PageResponse<AttendanceResponseDto>>> getEmployeeAttendance(
									@PathVariable Long employeeId,
									@RequestParam(required=false)
									@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
									@RequestParam(required=false)
									@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,
									@RequestParam(defaultValue="0") int page,
									@RequestParam(defaultValue="20") int size
			){
		
		PageResponse<AttendanceResponseDto> response= attendanceService.getEmployeeAttendance(employeeId, startDate, endDate, page, size);
		
		return ResponseEntity.ok(ApiResponse.success("Attendance records retrieved", response));
	}
	
	@GetMapping("/today")
	public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getTodayAttendance(){
		List<AttendanceResponseDto> response=attendanceService.getTodayAttendance();
		return ResponseEntity.ok(ApiResponse.success("Today's attendance retrieved",response));
	}
	
}
