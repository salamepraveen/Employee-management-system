package com.ems.payroll.client;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDto {
		
	 private Long id;
	    private Long employeeId;
	    private String leaveType;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private Integer totalDays;
	    private String status;
	    private String reason;
}
