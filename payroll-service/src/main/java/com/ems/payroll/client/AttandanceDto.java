package com.ems.payroll.client;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttandanceDto {
		private Long id;
		private Long employeeId;
		private LocalDate date;
		private LocalDateTime checkIn;
		private LocalDateTime checkOut;
		private String status;
		private Double hoursWorked;
}
