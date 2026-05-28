package com.ems.employee.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.entity.Employee;

@Mapper(componentModel="spring")
public interface EmployeeMapper {
		
	@Mapping(target="fullname", expression="java(employee.getFirstName() + \" \"+employee.getLastName())")
	@Mapping(target="department", source="department", qualifiedByName="departmentToString")
	@Mapping(target="status",source="status",qualifiedByName="statusToString")
	EmployeeResponseDto toDto(Employee employee);
	
	 @Mapping(target = "id", ignore = true)
	 @Mapping(target = "createdAt", ignore = true)
	 @Mapping(target = "updatedAt", ignore = true)
	 @Mapping(target = "status",ignore=true)
	 @Mapping(target = "department", source = "department")
	 Employee toEntity(EmployeeRequestDto dto);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target= "id", ignore=true)
	@Mapping(target="createdAt", ignore=true)
	@Mapping(target="updatedAt", ignore=true)
	@Mapping(target="status",ignore=true)
	void updateEntity(EmployeeRequestDto dto,@MappingTarget Employee employee);
	
	@Named("departmentToString")
	default String departmentToString(Employee.Department department) {
		return department !=null ?department.name():null;
	}
	
	@Named("statusToString")
	default String statusToString(Employee.EmployeeStatus status) {
		return status!=null ? status.name():null;
	}
	
}
