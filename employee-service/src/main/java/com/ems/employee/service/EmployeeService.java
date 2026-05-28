package com.ems.employee.service;

import com.ems.common.dto.PageResponse;
import com.ems.common.exception.BusinessException;
import com.ems.common.exception.ResourceNotFoundException;
import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.entity.Employee;
import com.ems.employee.mapper.EmployeeMapper;
import com.ems.employee.repository.EmployeeRepository;
import jakarta.persistence.criteria.Predicate;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
//@RequiredArgsConstructor
public class EmployeeService {
			private final EmployeeRepository employeeRepository;
			private final EmployeeMapper employeeMapper;
			
			@Autowired
			public EmployeeService(EmployeeRepository employeeRepository,EmployeeMapper employeeMapper) {
				this.employeeRepository=employeeRepository;
				this.employeeMapper=employeeMapper;
			}
			
			public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
				if(employeeRepository.existByEmail(requestDto.getEmail())) {
					throw new BusinessException("Employee with email "+requestDto.getEmail()+" already exists",409);
				}
				
				Employee employee=employeeMapper.toEntity(requestDto);
				
				Employee savedEmployee=employeeRepository.save(employee);
				return employeeMapper.toDto(savedEmployee);
			} 
			
			@Transactional(readOnly=true)
			public EmployeeResponseDto getEmployeeById(Long id) {
				Employee employee = employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee","id",id));
				
				return employeeMapper.toDto(employee); 
			}
			
			@Transactional(readOnly = true)
			public PageResponse<EmployeeResponseDto> getAllEmployees(int page,int size,String sortBy,String sortDir){
				 Sort sort=sortDir.equalsIgnoreCase("desc")?Sort.by(Sort.Direction.DESC,sortBy):Sort.by(Sort.Direction.ASC,sortBy);
				 Pageable pageable=PageRequest.of(page, size,sort);
				 Page<Employee> employeePage=employeeRepository.findAll(pageable);
				 
				 Page<EmployeeResponseDto> dtoPage=employeePage.map(employeeMapper::toDto);
				 
				 return PageResponse.from(dtoPage);
			}
			
			public PageResponse<EmployeeResponseDto> searchEmployees(
							String name,
							Employee.Department department, 
							Employee.EmployeeStatus status,
							Double minSalary,Double maxSalary,
							int page, int size,String sortBy,String sortDir
							){
				Sort sort=sortDir.equalsIgnoreCase("desc")? Sort.by(Sort.Direction.DESC,sortBy): Sort.by(Sort.Direction.ASC,sortBy);
				
				Pageable pageable=PageRequest.of(page, size,sort);
				Specification<Employee> spec=(root,query,cb)->{
					List<Predicate> predicates=new ArrayList<>();
					if(name!=null && !name.trim().isEmpty()) {
						String namePattern="%"+name.trim().toLowerCase()+"%";
						Predicate firstNameLike=cb.like(cb.lower(root.get("firstName")), namePattern);
						Predicate lastNameLike=cb.like(cb.lower(root.get("lastName")), namePattern);
						predicates.add(cb.or(firstNameLike,lastNameLike));
					}
					
					if(department !=null) {
						predicates.add(cb.equal(root.get("department"),department));
					}
					
					if(status !=null) {
						predicates.add(cb.equal(root.get("status"), status));
					}
					
					if(minSalary!=null) {
						predicates.add(cb.greaterThanOrEqualTo(root.get("salary"),minSalary));
					}
					
					if(maxSalary != null) {
						predicates.add(cb.lessThanOrEqualTo(root.get("salary"), maxSalary));
					}
					
				
					
			
					 
				
					return cb.and(predicates.toArray(new Predicate[0]));
				
				};
				
				Page<Employee> employeePage = employeeRepository.findAll(spec,pageable);
				Page<EmployeeResponseDto> dtoPage=employeePage.map(employeeMapper::toDto);
				return PageResponse.from(dtoPage);
			
				
			}
			
			public EmployeeResponseDto update(Long id, EmployeeRequestDto requestDto) {
				Employee existingEmployee = employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee","id",id));
				
				if(requestDto.getEmail()!=null && !requestDto.getEmail().equals(existingEmployee.getEmail())&& employeeRepository.existByEmail(requestDto.getEmail())){
					throw new BusinessException(
							"Employee with email "+requestDto.getEmail()+" already exists",409
							);
				}
				
				Employee updatedEmployee = employeeRepository.save(existingEmployee);
				
				return employeeMapper.toDto(updatedEmployee);
			}
			
			public void deleteEmployee(Long id) {
				Employee employee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee","id",id));
				
				employeeRepository.deleteById(id);
			}
			
			
			@Transactional(readOnly=true)
			public Employee getEmployeeEntityById(Long id) {
				return employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee","id",id));
				
			}
			
			@Transactional(readOnly=true)
			public List<EmployeeResponseDto> getEmployeeByDepartment(Employee.Department department){
				return employeeRepository.findByDepartment(department).stream().map(employeeMapper::toDto).toList();
			}
			
			
}
