package com.ems.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ems.employee.entity.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee>{
	Optional<Employee> findByEmail(String email);
	List<Employee> findByFirstNameAndLastName(String firstName,String lastName);
	List<Employee> findByDepartment(Employee.Department department);
	List<Employee> findByStatus(Employee.EmployeeStatus status);
	List<Employee> findByFirstNameContainingIgnoreCase(String firstName);
	List<Employee> findByLastNameConatainingIgnoreCase(String lastName);
	boolean existByEmail(String email);
	
	@Query("SELECT e FROM Employee e WHERE e.department= :department AND e.salary >= :minSalary")
	List<Employee> findByDepartmentAndMinSalary(@Param("department") Employee.Department department,@Param("minSalry") Double minSalary);
	
	long countByDepartment(Employee.Department department);
	

}
