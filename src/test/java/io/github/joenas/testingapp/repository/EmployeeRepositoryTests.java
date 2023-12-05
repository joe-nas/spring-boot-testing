package io.github.joenas.testingapp.repository;

import io.github.joenas.testingapp.model.Employee;
import org.aspectj.weaver.ast.ITestVisitor;
import org.hibernate.query.sqm.mutation.internal.cte.CteInsertStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest is a specialized test annotation that can be used to test JPA applications.
// It provides the following features:
// - Configuring H2, an in-memory database
// - Setting Hibernate, Spring Data, and the DataSource
// - Performing an @EntityScan
// - Turning on SQL logging
@DataJpaTest
public class EmployeeRepositoryTests {


    @Autowired
    private EmployeeRepository employeeRepository;

    // JUnit test for saving employee
    @DisplayName("JUnit test for saving employee")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe")
                .build();
        // when
        Employee savedEmployee = employeeRepository.save(employee);
        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for finding all employees
    @DisplayName("JUnit test for finding all employees")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        // given
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        // when
        List<Employee> employeesList = employeeRepository.findAll();

        // then
        assertThat(employeesList).isNotNull();
        assertThat(employeesList).hasSize(2);
    }

    @DisplayName("JUnit test for finding employee by id")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe")
                .build();
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
    public void givenEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe")
                .build();
        employeeRepository.save(employee);
        //when
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();
        //then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);

    }

    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmploye_thenReturnUpdatedEmployee() {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();
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
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();
        employeeRepository.save(employee);
        //when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        //then
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit test for custom query using JPQL with index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployee() {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();
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
    public void given_when_then() {
        //given

        //when

        //then

    }

}
