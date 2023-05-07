package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Get add employees
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    // Get employee info by id
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@NotBlank(message = "cannot be blank") @PathVariable(name = "employeeId") Long employeeId) {
        Optional<Employee> empOptional = Optional.ofNullable(employeeService.getEmployee(employeeId));
        return empOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Add new employee
    @PostMapping("/employees")
    public ResponseEntity<Employee> saveEmployee(@Validated @RequestBody Employee employee) {
        Employee empUpd = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(empUpd);
    }

    // Delete employee
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@NotBlank(message = "cannot be blank") @PathVariable(name = "employeeId") Long employeeId) {
        // delete object check
        boolean empExist = employeeService.checkEmployee(employeeId);
        if (empExist) {
            employeeService.deleteEmployee(employeeId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Update existing employee info
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@Validated @RequestBody Employee employee,
                                                   @NotBlank(message = "cannot be blank") @PathVariable(name = "employeeId") Long employeeId) {
        // update object check
        boolean empExist = employeeService.checkEmployee(employeeId);
        if (empExist) {
            employee.setId(employeeId);
            Employee empUpd = employeeService.updateEmployee(employee);
            return ResponseEntity.ok(empUpd);
        }
        return ResponseEntity.notFound().build();
    }
}
