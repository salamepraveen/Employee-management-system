package com.ems.payroll.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.payroll.entity.Payroll;


@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Long> {
	
	Optional<Payroll> findByEmployeeIdAndMonthAndYear(Long employeeId,Integer month,Integer year);
	boolean existsByEmployeeIdAndMonthAndYear(Long employeeId,Integer month,Integer year);
	
	List<Payroll> findByEmployeeIdOrderByYearDescMonthDesc(Long employeeId);
	List<Payroll> findByMonthAndYearOrderByEmployeeId(Integer month,Integer year);
	List<Payroll> findByStatus(Payroll.PayrollStatus status);
	
	
	
}
