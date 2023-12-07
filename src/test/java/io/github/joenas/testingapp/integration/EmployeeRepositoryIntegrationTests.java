package io.github.joenas.testingapp.integration;

import io.github.joenas.testingapp.model.Employee;
import io.github.joenas.testingapp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class EmployeeRepositoryIntegrationTests {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryIntegrationTests.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup(){

        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();
    }


    // JUnit test for saving employee
    @DisplayName("JUnit test for saving employee")
    @Test
    @Transactional
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given setup

        // when
        Employee savedEmployee = employeeRepository.save(employee);
        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for finding all employees
    @DisplayName("JUnit test for finding all employees")
    @Test
    @Transactional
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        // given setup and

        Employee employee1 = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        // when
        List<Employee> employeesList = employeeRepository.findAll();

        // then
        assertThat(employeesList).isNotNull();
        assertThat(employeesList).hasSize(2);
    }

    @DisplayName("JUnit test for finding employee by id")
    @Test
    @Transactional
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given setup

        // save method assigns an id to the employee object
        employeeRepository.save(employee);
        // when
        Employee employeeDB =  employeeRepository.findById(employee.getId()).get();
        // then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getId()).isEqualTo(employee.getId());
    }

    @DisplayName("JUnit test for getting employee by email")
    @Test
    @Transactional
    public void givenEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given setup

        employeeRepository.save(employee);
        //when
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();
        //then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);

    }

    @DisplayName("JUnit test for update employee operation")
    @Test
    @Transactional
    public void givenEmployeeObject_whenUpdateEmploye_thenReturnUpdatedEmployee() {
        //given setup

        employeeRepository.save(employee);
        //when
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("jane@doe.com");
        savedEmployee.setFirstName("Jane");
        Employee updatedEmployee =  employeeRepository.save(savedEmployee);
        //then
        assertThat(updatedEmployee.getEmail()).isEqualTo("jane@doe.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");
    }

    @DisplayName("JUnit test for delete employee operation")
    @Test
    @Transactional
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        //given setup

        employeeRepository.save(employee);
        //when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        //then
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit test for custom query using JPQL with index parameters")
    @Test
    @Transactional
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployee() {
        //given setup

        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Doe";
        //when
        Employee employeeDB = employeeRepository.findByJPQL(firstName, lastName);
        //then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstName()).isEqualTo(firstName);
        assertThat(employeeDB.getLastName()).isEqualTo(lastName);
    }

    @DisplayName("JUnit test for custom query using JPQL with named parameters")
    @Test
    @Transactional
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployee() {
        //given setup

        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Doe";
        //when
        Employee employeeDB = employeeRepository.findByJPQLNamedParams(firstName, lastName);
        //then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstName()).isEqualTo(firstName);
        assertThat(employeeDB.getLastName()).isEqualTo(lastName);
    }

    @DisplayName("JUnit test for custom query using SQL with index parameters")
    @Test
    @Transactional
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployee() {
        //given setup

        employeeRepository.save(employee);
        //when
        Employee employeeDB = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());
        //then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeDB.getLastName()).isEqualTo(employee.getLastName());
    }

    @DisplayName("JUnit test for custom query using SQL with named parameters")
    @Test
    @Transactional
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployee() {
        //given setup

        employeeRepository.save(employee);
        //when
        Employee employeeDB = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());
        //then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeDB.getLastName()).isEqualTo(employee.getLastName());
    }


}
