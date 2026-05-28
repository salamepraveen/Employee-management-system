package com.ems.payroll.entity;

import java.time.LocalDateTime;

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
@Table(name="payroll", uniqueConstraints= {@UniqueConstraint(name="uq_employee_month_year",columnNames= {"employee_id","month","year"})})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {
		
	@Column(name="employee_id",nullable=false)
	private Long employeeId;
	
	@Column(nullable=false)
	private Integer month;
	
	@Column(nullable=false)
	private Integer year;
	
	@Column(name="basic_salary", nullable=false, precision=12,scale=2)
	private Double basicSalary;
	
	@Column(name = "days_present")
	private Integer daysPresent;
	
	@Column(name = "days_absent")
	private Integer daysAbsent;
	
	 @Column(name="leave_deduction",precision=12,scale=2)
	 @Builder.Default
	 private Double leaveDeduction=0.0;
	 
	 @Column(precision=12, scale=2)
	 @Builder.Default
	 private Double overtime=0.0;
	 
	 @Column(precision=12,scale=2)
	 @Builder.Default
	 private Double bonus=0.0;
	 
	 @Column(name="gross_salary",precision=12,scale=2)
	 private Double grossSalary;
	 
	 @Column(precision=12,scale=2)
	 @Builder.Default
	 private Double tax=0.0;
	 
	 @Column(precision=12,scale=2)
	 @Builder.Default
	 private Double deductions=0.0;
	 
	 @Column(name="net_salary",precision=12,scale=2)
	 private Double netSalary;
	 
	 public enum PayrollStatus {
	        GENERATED, APPROVED, PAID, REJECTED
	    }
	 @Enumerated(EnumType.STRING)
	 @Column(nullable = false, length = 20)
	 @Builder.Default
	 private PayrollStatus status=PayrollStatus.GENERATED;
	 
	 @Column(name="approved_by")
	 private Long approvedBy;
	 
	 
	 @Column(name="paid_on")
	 private LocalDateTime paidOn;
	 
	 @Column(name="payment_month", nullable=false, length=20)
	 private String paymentMonth;
	 
	 
	 
	 
}
