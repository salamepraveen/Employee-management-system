package com.ems.attendance.service;

import com.ems.attendance.client.EmployeeClient;
import com.ems.attendance.client.EmployeeDto;
import com.ems.attendance.dto.*;
import com.ems.attendance.entity.Attendance;
import com.ems.attendance.repository.AttendanceRepository;
import com.ems.common.dto.ApiResponse;
import com.ems.common.dto.PageResponse;
import com.ems.common.exception.BusinessException;
import com.ems.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeClient employeeClient;

  
    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeClient employeeClient) {
        this.attendanceRepository = attendanceRepository;
        this.employeeClient = employeeClient;
    }

    public AttendanceResponseDto checkIn(CheckInRequest request) {

        
     
        EmployeeDto employee;
        try {
            ApiResponse<EmployeeDto> response = employeeClient.getEmployeeById(request.getEmployeeId());
            if (response.getData() == null) {
                throw new ResourceNotFoundException("Employee", "id", request.getEmployeeId());
            }
            employee = (EmployeeDto) response.getData();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Employee", "id", request.getEmployeeId());
        }

       
        LocalDate today = request.getCheckIn() != null
                ? request.getCheckIn().toLocalDate()
                : LocalDate.now();

        if (attendanceRepository.existsByEmployeeIdAndDate(request.getEmployeeId(), today)) {
            throw new BusinessException(
                    "Employee has already checked in today",
                    409 
            );
        }

       
        LocalDateTime checkInTime = request.getCheckIn() != null
                ? request.getCheckIn()
                : LocalDateTime.now();

     
        Attendance.AttendanceStatus status;
        if (checkInTime.toLocalTime().isAfter(LocalTime.of(9, 30))) {
            status = Attendance.AttendanceStatus.LATE;
        } else {
            status = Attendance.AttendanceStatus.CHECKED_IN;
        }

        
        Attendance attendance = Attendance.builder()
                .employeeId(request.getEmployeeId())
                .date(today)
                .checkIn(checkInTime)
                .status(status)
                .hoursWorked(0.0)
                .build();

        attendance = attendanceRepository.save(attendance);

       
        return buildAttendanceResponse(attendance, employee);
    }

 
    public AttendanceResponseDto checkOut(CheckOutRequest request) {

       
        LocalDate today = request.getCheckOut() != null
                ? request.getCheckOut().toLocalDate()
                : LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(request.getEmployeeId(), today)
                .orElseThrow(() -> new BusinessException(
                        "No check-in record found for today. Please check in first.",
                        400
                ));

       
        if (attendance.getCheckOut() != null) {
            throw new BusinessException(
                    "Employee has already checked out today",
                    409
            );
        }

       
        LocalDateTime checkOutTime = request.getCheckOut() != null
                ? request.getCheckOut()
                : LocalDateTime.now();

        attendance.setCheckOut(checkOutTime);

        long minutesWorked = Duration.between(attendance.getCheckIn(), checkOutTime).toMinutes();
        double hoursWorked = minutesWorked / 60.0;
        attendance.setHoursWorked(hoursWorked);

       
        if (hoursWorked < 4.0) {
            attendance.setStatus(Attendance.AttendanceStatus.HALF_DAY);
        } else {
            attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        }

        attendance = attendanceRepository.save(attendance);

       
        EmployeeDto employee = getEmployeeInfo(request.getEmployeeId());
        return buildAttendanceResponse(attendance, employee);
    }

   
    @Transactional(readOnly = true)
    public PageResponse<AttendanceResponseDto> getEmployeeAttendance(
            Long employeeId, LocalDate startDate, LocalDate endDate,
            int page, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Attendance> records;

        if (startDate != null && endDate != null) {
         
            records = attendanceRepository
                    .findByEmployeeIdAndDateBetweenOrderByDateDesc(
                            employeeId, startDate, endDate);
        } else {
          
            records = attendanceRepository
                    .findByEmployeeIdOrderByDateDesc(employeeId);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), records.size());
        List<Attendance> pageContent = start > records.size() ? List.of() : records.subList(start, end);
        Page<Attendance> attendancePage = new PageImpl<>(pageContent, pageable, records.size());


      
        List<AttendanceResponseDto> dtos = attendancePage.getContent().stream()
                .map(a -> {
                    EmployeeDto emp = getEmployeeInfo(a.getEmployeeId());
                    return buildAttendanceResponse(a, emp);
                })
                .toList();

        Page<AttendanceResponseDto> dtoPage = new PageImpl<>(
                dtos, pageable, attendancePage.getTotalElements());

        return PageResponse.from(dtoPage);
    }


    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> getTodayAttendance() {
        List<Attendance> records = attendanceRepository.findByDate(LocalDate.now());

        return records.stream()
                .map(a -> {
                    EmployeeDto emp = getEmployeeInfo(a.getEmployeeId());
                    return buildAttendanceResponse(a, emp);
                })
                .toList();
    }
    private EmployeeDto getEmployeeInfo(Long employeeId) {
        try {
            ApiResponse<EmployeeDto> response = employeeClient.getEmployeeById(employeeId);
            if (response.getData() != null) {
                return (EmployeeDto) response.getData();
            }
        } catch (Exception e) {
            
            System.out.println("Could not fetch employee info for ID: " + employeeId);
        }
        return null;
    }

 
    private AttendanceResponseDto buildAttendanceResponse(
            Attendance attendance, EmployeeDto employee) {

        return AttendanceResponseDto.builder() 
                .id(attendance.getId())
                .employeeId(attendance.getEmployeeId())
                .employeeName(employee != null
                        ? employee.getFristName() + " " + employee.getLastName()
                        : "Unknown")
                .department(employee != null ? employee.getDepartment() : "Unknown")
                .date(attendance.getDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .status(attendance.getStatus().name())
                .hoursWorked(attendance.getHoursWorked())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}