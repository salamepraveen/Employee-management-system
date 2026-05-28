package com.ems.payroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.common.exception.BusinessException;
import com.ems.payroll.client.AttendanceClient;
import com.ems.payroll.client.EmployeeClient;
import com.ems.payroll.client.LeaveClient;
import com.ems.payroll.dto.PayrollRequestDto;
import com.ems.payroll.dto.PayrollResponseDto;
import com.ems.payroll.repository.PayrollRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PayrollService {
		private final PayrollRepository payrollRepository;
		private final EmployeeClient employeeClient;
		private final AttendanceClient attendanceClient;
		private final LeaveClient leaveClient;
		
		@Autowired
	    public PayrollService(PayrollRepository payrollRepository,
	                          EmployeeClient employeeClient,
	                          AttendanceClient attendanceClient,
	                          LeaveClient leaveClient) {
	        this.payrollRepository = payrollRepository;
	        this.employeeClient = employeeClient;
	        this.attendanceClient = attendanceClient;
	        this.leaveClient = leaveClient;
	    }
		
		public PayrollResponseDto generatePayroll(PayrollRequestDto dto) {
			
			if(payrollRepository.existsByEmployeeIdAndMonthAndYear(dto.getEmployeeId(),dto.getMonth(),dto.getYear())) {
				throw new BusinessException("Payroll already generated for employee "+
			dto.getEmployeeId()+" for "+dto.getMonth()+" "+dto.getYear(),409);
			} 
		}
}
