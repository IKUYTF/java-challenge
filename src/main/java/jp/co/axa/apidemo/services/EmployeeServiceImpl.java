package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@CacheConfig(cacheNames = EmployeeServiceImpl.EMPLOYEES_CACHE)
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    public static final String EMPLOYEES_CACHE = "employees";

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Get all employees' info
     */
    @Override
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        log.info("Fetching all employees");
        return employees;
    }

    /**
     * Get employee info by employee id
     */
    @Override
    @Cacheable(key = "#employeeId")
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optionalEmployee  = employeeRepository.findById(employeeId);
        log.info("Fetching employee by id: {}", employeeId);
        return optionalEmployee .orElse(null);
    }

    /**
     * add new employee info and refresh cache
     */
    @Override
    @Transactional
    @CachePut(key = "#employee.id")
    public Employee saveEmployee(Employee employee){
        employeeRepository.save(employee);
        log.info("Add new employee {}", employee.getId());
        return employee;
    }

    /**
     * delete existing employee info and refresh cache
     */
    @Override
    @Transactional
    @CacheEvict(value = "employees", key = "#employeeId")
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
        log.info("Delete employee by id: {}", employeeId);
    }

    /**
     * Update existing employee info and refresh cache
     */
    @Override
    @Transactional
    @CachePut(key = "#employee.id")
    public Employee updateEmployee(Employee employee) {
        employeeRepository.save(employee);
        log.info("Update employee by id: {}", employee.getId());
        return employee;
    }


    /**
     * Check employee id whether exist
     * @param employeeId employ id
     * @return true: existing
     */
    @Override
    public boolean checkEmployee(Long employeeId) {
        boolean existsById = employeeRepository.existsById(employeeId);
        if (!existsById) {
            log.info("Employee object does not exist. Id: {}", employeeId);
        }
        return existsById;
    }
}