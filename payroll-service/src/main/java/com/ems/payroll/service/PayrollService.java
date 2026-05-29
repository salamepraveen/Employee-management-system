package com.ems.payroll.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.common.dto.ApiResponse;
import com.ems.common.dto.PageResponse;
import com.ems.common.exception.BusinessException;
import com.ems.common.exception.ResourceNotFoundException;
import com.ems.payroll.client.AttendanceClient;
import com.ems.payroll.client.EmployeeClient;
import com.ems.payroll.client.EmployeeDto;
import com.ems.payroll.client.LeaveClient;
import com.ems.payroll.client.AttandanceDto;
import com.ems.payroll.client.LeaveDto;
import com.ems.payroll.dto.PayrollRequestDto;
import com.ems.payroll.dto.PayrollResponseDto;
import com.ems.payroll.entity.Payroll;
import com.ems.payroll.repository.PayrollRepository;

import org.springframework.transaction.annotation.Transactional;

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
			
			EmployeeDto employee=getEmployeeInfo(dto.getEmployeeId());
			if(employee==null) {
				throw new ResourceNotFoundException("Employee","id",dto.getEmployeeId());
			}
			
			YearMonth yearMonth=YearMonth.of(dto.getYear(), dto.getMonth());
			LocalDate startDate=yearMonth.atDay(1);
			LocalDate endDate=yearMonth.atEndOfMonth();
			
			int daysPresent = 0;
			int daysAbsent = 0;
			
			try {
				ApiResponse<PageResponse<AttandanceDto>> attendanceResponse = attendanceClient.getEmployeeAttendance(dto.getEmployeeId(), startDate.toString(), endDate.toString());
				if (attendanceResponse != null && attendanceResponse.getData() != null) {
					PageResponse<AttandanceDto> pageResponse = attendanceResponse.getData();
					if (pageResponse.getContent() != null) {
						for (AttandanceDto att : pageResponse.getContent()) {
							if ("PRESENT".equalsIgnoreCase(att.getStatus()) || "CHECKED_IN".equalsIgnoreCase(att.getStatus()) || "LATE".equalsIgnoreCase(att.getStatus())) {
								daysPresent++;
							}
						}
					}
				}
			} catch(Exception e) {
				System.out.println("Could not fetch attendance data: " + e.getMessage());
			}
			
			int workingDays = 0;
			LocalDate current = startDate;
			while (!current.isAfter(endDate)) {
				java.time.DayOfWeek day = current.getDayOfWeek();
				if (day != java.time.DayOfWeek.SATURDAY && day != java.time.DayOfWeek.SUNDAY) {
					workingDays++;
				}
				
				current = current.plusDays(1);
			}
			
			if (daysPresent == 0) {
				daysPresent = workingDays;
			}
			
			daysAbsent = workingDays - daysPresent;
			
			int approvedLeaveDays = 0;
	        try {
	            ApiResponse<List<LeaveDto>> leaveResponse = leaveClient.getEmployeeLeaves(dto.getEmployeeId());
	            if (leaveResponse != null && leaveResponse.getData() != null) {
	                List<LeaveDto> leaves = leaveResponse.getData();
	                for (LeaveDto leave : leaves) {
	                    if ("APPROVED".equalsIgnoreCase(leave.getStatus())) {
	                        LocalDate leaveStart = leave.getStartDate();
	                        LocalDate leaveEnd = leave.getEndDate();
	                        
	                        LocalDate overlapStart = leaveStart.isBefore(startDate) ? startDate : leaveStart;
	                        LocalDate overlapEnd = leaveEnd.isAfter(endDate) ? endDate : leaveEnd;
	                        
	                        if (!overlapStart.isAfter(overlapEnd)) {
	                            LocalDate temp = overlapStart;
	                            while (!temp.isAfter(overlapEnd)) {
	                                java.time.DayOfWeek day = temp.getDayOfWeek();
	                                if (day != java.time.DayOfWeek.SATURDAY && day != java.time.DayOfWeek.SUNDAY) {
	                                    approvedLeaveDays++;
	                                }
	                                temp = temp.plusDays(1);
	                            }
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("Could not fetch leave data: " + e.getMessage());
	        }
			
	        double basicSalary = employee.getSalary();
	        double perDayRate = basicSalary / workingDays;

	        int unpaidAbsences = Math.max(0, daysAbsent - approvedLeaveDays);
	        double leaveDeduction = perDayRate * unpaidAbsences;

	        double overtime = 0.0;  // No overtime tracking for now
	        double bonus = dto.getBonus() != null ? dto.getBonus() : 0.0;
	        double grossSalary = basicSalary - leaveDeduction + overtime + bonus;

	        double tax = calculateTax(grossSalary);
	        double deductions = dto.getDeductions() != null ? dto.getDeductions() : 500.0;
	        double netSalary = grossSalary - tax - deductions;
	        
	        
	        Payroll payroll = Payroll.builder()
	                .employeeId(dto.getEmployeeId())
	                .month(dto.getMonth())
	                .year(dto.getYear())
	                .paymentMonth(getMonthName(dto.getMonth()) + " " + dto.getYear())
	                .basicSalary(basicSalary)
	                .daysPresent(daysPresent)
	                .daysAbsent(daysAbsent)
	                .leaveDeduction(leaveDeduction)
	                .overtime(overtime)
	                .bonus(bonus)
	                .grossSalary(grossSalary)
	                .tax(tax)
	                .deductions(deductions)
	                .netSalary(netSalary)
	                .status(Payroll.PayrollStatus.GENERATED)
	                .build();
	        
	        payroll = payrollRepository.save(payroll);
	        
	        return buildPayrollResponse(payroll, employee, workingDays, approvedLeaveDays);
		}
		
		  private double calculateTax(double grossSalary) {
		        double tax = 0.0;

		        if (grossSalary <= 25000) {
		            tax = 0;
		        } else if (grossSalary <= 50000) {
		            tax = (grossSalary - 25000) * 0.05;
		        } else if (grossSalary <= 100000) {
		            tax = 25000 * 0.05;                          // First slab
		            tax += (grossSalary - 50000) * 0.10;          // Second slab
		        } else {
		            tax = 25000 * 0.05;                          // First slab
		            tax += 50000 * 0.10;                         // Second slab
		            tax += (grossSalary - 100000) * 0.15;        // Third slab
		        }

		        // Round to 2 decimal places
		        return Math.round(tax * 100.0) / 100.0;
		    }
		  
		  
		  @Transactional(readOnly = true)
		    public PayrollResponseDto getPayroll(Long id) {
		        Payroll payroll = payrollRepository.findById(id)
		                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", id));

		        EmployeeDto employee = getEmployeeInfo(payroll.getEmployeeId());
		        return buildPayrollResponse(payroll, employee, 0, 0);
		    }

		    @Transactional(readOnly = true)
		    public List<PayrollResponseDto> getEmployeePayrollHistory(Long employeeId) {
		        List<Payroll> records = payrollRepository
		                .findByEmployeeIdOrderByYearDescMonthDesc(employeeId);

		        return records.stream()
		                .map(p -> {
		                    EmployeeDto emp = getEmployeeInfo(p.getEmployeeId());
		                    return buildPayrollResponse(p, emp, 0, 0);
		                })
		                .toList();
		    }

		    public PayrollResponseDto approvePayroll(Long payrollId, Long managerId) {
		        Payroll payroll = payrollRepository.findById(payrollId)
		                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", payrollId));

		        if (payroll.getStatus() != Payroll.PayrollStatus.GENERATED) {
		            throw new BusinessException(
		                    "Payroll can only be approved when in GENERATED status. " +
		                    "Current status: " + payroll.getStatus(), 409);
		        }

		        payroll.setStatus(Payroll.PayrollStatus.APPROVED);
		        payroll.setApprovedBy(managerId);
		        payroll = payrollRepository.save(payroll);

		        EmployeeDto employee = getEmployeeInfo(payroll.getEmployeeId());
		        return buildPayrollResponse(payroll, employee, 0, 0);
		    }
		    
		    private EmployeeDto getEmployeeInfo(Long employeeId) {
		        try {
		            var response = employeeClient.getEmployeeById(employeeId);
		            if (response != null && response.getData() != null) {
		                return (EmployeeDto) response.getData();
		            }
		        } catch (Exception e) {
		            System.out.println("Could not fetch employee info for ID: " + employeeId);
		        }
		        return null;
		    }

		    private PayrollResponseDto buildPayrollResponse(
		            Payroll payroll, EmployeeDto employee,
		            int workingDays, int leaveDays) {

		        double totalDeductions = payroll.getLeaveDeduction()
		                + payroll.getTax() + payroll.getDeductions();

		        return PayrollResponseDto.builder()
		                .id(payroll.getId())
		                .employeeId(payroll.getEmployeeId())
		                .employeeName(employee != null
		                        ? employee.getFirstName() + " " + employee.getLastName()
		                        : "Unknown")
		                .department(employee != null ? employee.getDepartment() : "Unknown")
		                .position(employee != null ? employee.getPosition() : "Unknown")
		                .month(payroll.getMonth())
		                .year(payroll.getYear())
		                .paymentMonth(payroll.getPaymentMonth())
		                .basicSalary(payroll.getBasicSalary())
		                .overtime(payroll.getOvertime())
		                .bonus(payroll.getBonus())
		                .grossSalary(payroll.getGrossSalary())
		                .leaveDeduction(payroll.getLeaveDeduction())
		                .tax(payroll.getTax())
		                .deductions(payroll.getDeductions())
		                .totalDeductions(totalDeductions)
		                .netSalary(payroll.getNetSalary())
		                .daysPresent(payroll.getDaysPresent())
		                .daysAbsent(payroll.getDaysAbsent())
		                .workingDays(workingDays)
		                .leaveDays(leaveDays)
		                .status(payroll.getStatus().name())
		                .createdAt(payroll.getCreatedAt())
		                .build();
		    }

		    private String getMonthName(int month) {
		        String[] months = {
		            "", "January", "February", "March", "April", "May", "June",
		            "July", "August", "September", "October", "November", "December"
		        };
		        return months[month];
		    }
}
