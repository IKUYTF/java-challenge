package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> retrieveEmployees();

    Employee getEmployee(Long employeeId);

    Employee saveEmployee(Employee employee);

    void deleteEmployee(Long employeeId);

    Employee updateEmployee(Employee employee);

    boolean checkEmployee(Long employeeId);
}