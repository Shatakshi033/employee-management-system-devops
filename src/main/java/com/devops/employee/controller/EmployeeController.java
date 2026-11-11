package com.devops.employee.controller;
import com.devops.employee.entity.Employee;
import com.devops.employee.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController @RequestMapping("/api/employees")
public class EmployeeController {
 private final EmployeeRepository repo;
 public EmployeeController(EmployeeRepository repo){this.repo=repo;}
 @GetMapping public List<Employee> all(){return repo.findAll();}
 @GetMapping("/{id}") public Employee one(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found"));}
 @PostMapping @ResponseStatus(HttpStatus.CREATED)
 public Employee create(@Valid @RequestBody Employee e){
  if(repo.existsByEmail(e.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
  e.setId(null); return repo.save(e);
 }
 @PutMapping("/{id}") public Employee update(@PathVariable Long id,@Valid @RequestBody Employee input){
  Employee e=one(id);
  if(!e.getEmail().equals(input.getEmail()) && repo.existsByEmail(input.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
  e.setFirstName(input.getFirstName()); e.setLastName(input.getLastName()); e.setEmail(input.getEmail());
  e.setDepartment(input.getDepartment()); e.setSalary(input.getSalary()); return repo.save(e);
 }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
 public void delete(@PathVariable Long id){if(!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found"); repo.deleteById(id);}
}
