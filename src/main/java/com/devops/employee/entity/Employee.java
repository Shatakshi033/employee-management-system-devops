package com.devops.employee.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
@Entity @Table(name="employees")
public class Employee {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Column(nullable=false) private String firstName;
 @NotBlank @Column(nullable=false) private String lastName;
 @Email @NotBlank @Column(nullable=false, unique=true) private String email;
 @NotBlank @Column(nullable=false) private String department;
 @PositiveOrZero @Column(nullable=false, precision=12, scale=2) private BigDecimal salary;
 public Employee() {}
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getFirstName(){return firstName;} public void setFirstName(String v){firstName=v;}
 public String getLastName(){return lastName;} public void setLastName(String v){lastName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getDepartment(){return department;} public void setDepartment(String v){department=v;}
 public BigDecimal getSalary(){return salary;} public void setSalary(BigDecimal v){salary=v;}
}
