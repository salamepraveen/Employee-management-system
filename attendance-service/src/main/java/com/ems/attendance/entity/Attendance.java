package com.ems.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ems.common.enitity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="attendance", uniqueConstraints= {@UniqueConstraint(name="uq_employee_date",columnNames= {"employee_id","date"})})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor


public class Attendance extends BaseEntity{
		
	@Column(name="employee_id", nullable=false)
	private Long employeeId;
	
	
	@Column(nullable=false)
	private LocalDate date;
	
	@Column(name="check_in")
	private LocalDateTime checkIn;
	
	@Column(name="check_out")
	private LocalDateTime checkOut;
	
	public enum AttendanceStatus{
		PRESENT,CHECKED_IN,ABSENT,LATE,HALF_DAY,ON_LEAVE
	}
	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false,length=20)
	@Builder.Default
	private AttendanceStatus status =  AttendanceStatus.ABSENT;
	
	@Column(name="hours_worked", precision=4,scale=2)
	@Builder.Default
	private Double hoursWorked =0.0;
}
