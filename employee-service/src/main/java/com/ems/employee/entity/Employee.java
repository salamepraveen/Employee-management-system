package com.ems.employee.entity;

import java.time.LocalDate;

import com.ems.common.enitity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="employees")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class Employee extends BaseEntity{
			
		@Column(name="first_name",nullable=false,length=50)
		private String firstName;
		@Column(name="last_name",nullable=false,length=50)
		private String lastName;
		@Column(nullable=false,unique=true,length=100)
		private String email;
		
		@Column(length=20)
		private String phone;
		
		public enum Department{
			HR,ENGINEERING,FINANCE,MARKETING,OPERATIONS
		}
		
		@Enumerated(EnumType.STRING)
		@Column(name="department",nullable=false,length=30)
		private Department department;
		
		@Column(nullable=false,length=50)
		private String position;
		
		@Column(name="hire_date",nullable=false)
		private LocalDate hireDate;
		
		@Column(nullable=false,precision=10,scale=2)
		private Double salary;
		
		public enum EmployeeStatus{
			ACTIVE,INACTIVE,ON_LEAVE,TERMINATED
		}
		
		@Enumerated(EnumType.STRING)
		@Column(nullable=false,length=20)
		@Builder.Default
		private EmployeeStatus status=EmployeeStatus.ACTIVE;
}
