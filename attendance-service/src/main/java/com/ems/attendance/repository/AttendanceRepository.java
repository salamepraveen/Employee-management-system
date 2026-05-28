package com.ems.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.attendance.entity.Attendance;



public interface AttendanceRepository extends JpaRepository<Attendance,Long>, JpaSpecificationExecutor<Attendance> {
		
	Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
	
	boolean existsByEmployeeIdAndDate(Long employeeId, LocalDate date);
	
	List<Attendance> findByEmployeeIdOrderByDateDesc(Long employeeId);
	
	List<Attendance> findByEmployeeIdAndDateBetweenOrderByDateDesc(Long employeeId, LocalDate startDate,LocalDate endDate);
	
	List<Attendance> findByDate(LocalDate date);
	
	Long countByEmployeeIdAndDateBetweenAndStatus(Long employeeIId, LocalDate startDate, LocalDate endDate, Attendance.AttendanceStatus status);
	
	@Query("SELECT a.date, a.status, a.checkIN, a.checkOut, a.hoursWorked "+
	"From Attendance a "+ 
	"Where a.employeeId= :employeeId And a.date BETWEEN :startDate AND :endDate "+
	"ORDER BY a.date DESC")
	List<Object[]> getAttendanceSummary(
			@Param("employeeId") Long employeeId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate
			);
}
