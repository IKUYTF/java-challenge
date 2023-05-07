package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    private HttpHeaders headers;

    // fill test data before test
    @Before
    public void setup() {
        headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");

        Employee employee = new Employee();
        employee.setName("Jerry");
        employee.setDepartment("Finance");
        employee.setSalary(30000);
        employeeRepository.save(employee);
    }

    /**
     * test the api access with incorrect user/pw
     */
    @Test
    public void testBasicAuth() {
        headers.setBasicAuth("admin1", "admin1");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.GET,
                entity,
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * test get employee info by id
     */
    @Test
    public void testGetEmployeeById() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.GET,
                entity,
                Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Jerry");
    }

    /**
     * test get employee info by id not exist
     */
    @Test
    public void testGetEmployeeByIdNotExist() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees/1111",
                HttpMethod.GET,
                entity,
                Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * test get employee info by id with blank id
     */
    @Test
    public void testGetEmployeeByIdBlankId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees/ ",
                HttpMethod.GET,
                entity,
                Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * test get all employees
     */
    @Test
    public void testGetAllEmployees() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<List<Employee>> responseType = new ParameterizedTypeReference<List<Employee>>() {};
        ResponseEntity<List<Employee>> response = restTemplate.exchange(
                "/api/v1/employees",
                HttpMethod.GET,
                entity,
                responseType);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isNotEqualTo(0);
    }

    /**
     * test add new employee
     */
    @Test
    public void testSaveEmployee() {
        String requestBody = "{\"name\":\"Fancy\", \"department\":\"engineering\", \"salary\":40000}";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees",
                HttpMethod.POST,
                entity,
                Employee.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Fancy");
    }

    /**
     * test add new employee with blank request param
     */
    @Test
    public void testSaveEmployeeBlankReqParam() {
        String requestBody = "{\"name\":\"\", \"department\":\"engineering\", \"salary\":40000}";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees",
                HttpMethod.POST,
                entity,
                Employee.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * test update employee
     */
    @Test
    public void testUpdateEmployee() {
        String requestBody = "{\"name\":\"Fancy\", \"department\":\"engineering\", \"salary\":\"50000\"}";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees/2",
                HttpMethod.PUT,
                entity,
                Employee.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Fancy");
    }

    /**
     * test update employee not exist
     */
    @Test
    public void testUpdateEmployeeNotExist() {
        String requestBody = "{\"name\":\"Fancy\", \"department\":\"engineering\", \"salary\":\"50000\"}";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees/22222",
                HttpMethod.PUT,
                entity,
                Employee.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * test update employee with blank id
     */
    @Test
    public void testUpdateEmployeeBlankId() {
        String requestBody = "{\"name\":\"Fancy\", \"department\":\"engineering\", \"salary\":\"50000\"}";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees/ ",
                HttpMethod.PUT,
                entity,
                Employee.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * test update employee with blank request param
     */
    @Test
    public void testUpdateEmployeeRequestParamBlank() {
        String requestBody = "{\"name\":\"\", \"department\":\"engineering\", \"salary\":\"50000\"}";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.PUT,
                entity,
                Employee.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * test delete employee
     */
    @Test
    public void testDeleteEmployee() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/employees/3",
                HttpMethod.DELETE,
                entity,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * test delete employee not exist
     */
    @Test
    public void testDeleteEmployeeNotExist() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/employees/11111",
                HttpMethod.DELETE,
                entity,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * test delete employee with blank id
     */
    @Test
    public void testDeleteEmployeeBlankId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/employees/ ",
                HttpMethod.DELETE,
                entity,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
