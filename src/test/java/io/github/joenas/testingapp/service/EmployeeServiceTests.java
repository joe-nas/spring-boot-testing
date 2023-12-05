package io.github.joenas.testingapp.service;

import io.github.joenas.testingapp.model.Employee;
import io.github.joenas.testingapp.repository.EmployeeRepository;
import io.github.joenas.testingapp.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    // JUnit test for saveEmployee merthod
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
}
