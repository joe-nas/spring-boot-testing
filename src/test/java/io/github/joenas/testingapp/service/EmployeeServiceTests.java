package io.github.joenas.testingapp.service;

import io.github.joenas.testingapp.exception.ResourceNotFoundException;
import io.github.joenas.testingapp.model.Employee;
import io.github.joenas.testingapp.repository.EmployeeRepository;
import io.github.joenas.testingapp.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


// @ExtendsWith is needed so that the class uses @Mock as well as @InjectMocks annotations
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;
    @BeforeEach
    public void setup(){

        // Represents @Mock annotation
//        employeeRepository = Mockito.mock(EmployeeRepository.class);

        // Represents @InjectMocks
//        employeeService = new EmployeeServiceImpl(employeeRepository);

        // id is needed because a user coming from db is mocked
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given
        // saveEmployee depends on a employee object and findByEmail and save methods
        // stubbing the methods the saveEmploye Service method depends on
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);
        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);
        //then
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for saveEmployee method with duplicate email
    @DisplayName("JUnit test for saveEmployee method which throws Exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given
        // saveEmployee depends on a employee object and findByEmail and save methods
        // stubbing the methods the saveEmploye Service method depends on
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

//        is not needed because it is not invoked after throwing the exception
//        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);
        //when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,
                () -> employeeService.saveEmployee(employee));
        //then
        verify(employeeRepository, never()).save(any(Employee.class));

    }

    @DisplayName("JUnit test for getAllEmployees method - positive scenario")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        //given
        List<Employee> employees = List.of(employee, Employee.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build());
        given(employeeRepository.findAll()).willReturn(employees);
        //when
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getAllEmployees method - negative scenario")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        //given
        List<Employee> employees = List.of(employee, Employee.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build());
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmplyeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given - precondition or setup
        long id = 1;
        given(employeeRepository.findById(id)).willReturn(Optional.of(employee));
        //when - action or the behaviour to test
        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(id);
        //then - verify the output
        Assertions.assertThat(optionalEmployee).isNotNull();
        Assertions.assertThat(optionalEmployee.get().getId()).isEqualTo(id);
    }

    @DisplayName("Junit test for updateEmployee operation")
    @Test
    public void givenEmplyee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Jane");
        employee.setEmail("jane@doe.com");
        //when - action or the behaviour to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        //then - verify the output
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("jane@doe.com");
    }

    @DisplayName("JUnit test for deleteEmployee operation")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnVoid() {
        //given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employee.getId());
        //when - action or the behaviour to test
        employeeService.deleteEmployee(employee.getId());
        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employee.getId());

    }

}
