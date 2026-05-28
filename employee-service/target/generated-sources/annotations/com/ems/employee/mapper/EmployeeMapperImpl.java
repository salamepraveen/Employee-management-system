package com.ems.employee.mapper;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.entity.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-28T23:34:36+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeResponseDto toDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeResponseDto.EmployeeResponseDtoBuilder employeeResponseDto = EmployeeResponseDto.builder();

        employeeResponseDto.department( departmentToString( employee.getDepartment() ) );
        employeeResponseDto.status( statusToString( employee.getStatus() ) );
        employeeResponseDto.createdAt( employee.getCreatedAt() );
        employeeResponseDto.email( employee.getEmail() );
        employeeResponseDto.firstName( employee.getFirstName() );
        employeeResponseDto.hireDate( employee.getHireDate() );
        employeeResponseDto.id( employee.getId() );
        employeeResponseDto.lastName( employee.getLastName() );
        employeeResponseDto.phone( employee.getPhone() );
        employeeResponseDto.position( employee.getPosition() );
        employeeResponseDto.salary( employee.getSalary() );
        employeeResponseDto.updatedAt( employee.getUpdatedAt() );

        employeeResponseDto.fullname( employee.getFirstName() + " "+employee.getLastName() );

        return employeeResponseDto.build();
    }

    @Override
    public Employee toEntity(EmployeeRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Employee.EmployeeBuilder<?, ?> employee = Employee.builder();

        employee.department( dto.getDepartment() );
        employee.email( dto.getEmail() );
        employee.firstName( dto.getFirstName() );
        employee.hireDate( dto.getHireDate() );
        employee.lastName( dto.getLastName() );
        employee.phone( dto.getPhone() );
        employee.position( dto.getPosition() );
        employee.salary( dto.getSalary() );

        return employee.build();
    }

    @Override
    public void updateEntity(EmployeeRequestDto dto, Employee employee) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getDepartment() != null ) {
            employee.setDepartment( dto.getDepartment() );
        }
        if ( dto.getEmail() != null ) {
            employee.setEmail( dto.getEmail() );
        }
        if ( dto.getFirstName() != null ) {
            employee.setFirstName( dto.getFirstName() );
        }
        if ( dto.getHireDate() != null ) {
            employee.setHireDate( dto.getHireDate() );
        }
        if ( dto.getLastName() != null ) {
            employee.setLastName( dto.getLastName() );
        }
        if ( dto.getPhone() != null ) {
            employee.setPhone( dto.getPhone() );
        }
        if ( dto.getPosition() != null ) {
            employee.setPosition( dto.getPosition() );
        }
        if ( dto.getSalary() != null ) {
            employee.setSalary( dto.getSalary() );
        }
    }
}
