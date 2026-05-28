package com.ems.leave.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.ems.common.enitity.BaseEntity;

@Entity
@Table(name="leave_request")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest extends BaseEntity {
		
	@Column(name="employee_id", nullable=false)
	private Long employeeId;
	
	public enum LeaveType{
			SICK, CASUAL, ANNUAL, MATERNITY, PATERNITY
	}
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="leave_type", nullable=false,length=20)
	private LeaveType leaveType;
	
	@Column(name="start_date", nullable=false)
	private LocalDate startDate;
	
	@Column(name="end_date", nullable=false)
	private LocalDate endDate;
	
	@Column(length=500)
	private String reason;
	
	
	public enum LeaveStatus{
		PENDING,APPROVED,REJECTED,CANCELLED
	}
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false,length=20)
	@Builder.Default
	private LeaveStatus status=LeaveStatus.PENDING;
	
	@Column(name="approved_by")
	private Long approvedBy;
	
	@Column(name="manager_comment", length=500)
	private String managerComment;
	
	@Column(name="total_days")
	private Integer totalDays;
}
