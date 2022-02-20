package Payroll.controller;

import Payroll.dto.Employee;
import Payroll.exception.EmployeeNotFoundException;
import Payroll.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<Employee> all(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Employee one(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PostMapping
    Employee newEmployee(@RequestBody Employee newEmployee){
        return repository.save(newEmployee);
    }

    @PutMapping("/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/{id}")
    void deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
    }

}
