package com.devops.employee.repository;
import com.devops.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmployeeRepository extends JpaRepository<Employee,Long> { boolean existsByEmail(String email); }
