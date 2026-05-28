 package com.ems.leave.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.leave.entity.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {
			List<LeaveRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
			List<LeaveRequest> findByEmployeeIdAndStartDateBetweenOrderByStartDateDesc(
					Long employeeId, LocalDate startDate,LocalDate endDate
					
					);
			List<LeaveRequest> findByStatusOrderByCreatedAtDesc(LeaveRequest.LeaveStatus status);
			
			@Query("SELECT lr FROM LeaveRequest lr "  +
					"WHERE lr.employeeId = :employeeId "+
					"AND lr.status= 'APPROVED' "+
					"AND lr.startDate <=:endDate"+
					"AND lr.endDate >=:startDate"
					)
			List<LeaveRequest> findOverlappingApprovedLeaves(
					@Param("employeeId") Long employeeId, 
					@Param("startDate") LocalDate startDate, 
					@Param("endDate") LocalDate endDate);
			 @Query("SELECT COALESCE(SUM(lr.totalDays), 0) FROM LeaveRequest lr " +
			           "WHERE lr.employeeId = :employeeId " +
			           "AND lr.leaveType = :leaveType " +
			           "AND lr.status = 'APPROVED' " +
			           "AND YEAR(lr.startDate) = :year")
			    long countLeaveDaysUsed(
			            @Param("employeeId") Long employeeId,
			            @Param("leaveType") LeaveRequest.LeaveType leaveType,
			            @Param("year") int year);
}
