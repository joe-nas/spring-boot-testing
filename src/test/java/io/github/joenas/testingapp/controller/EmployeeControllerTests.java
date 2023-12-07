package io.github.joenas.testingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.joenas.testingapp.model.Employee;
import io.github.joenas.testingapp.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class) // for Unit testing, only loads the controller layer to the application context
// Integration testing is done with @SpringBootTest which loads the whole application context
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc; // used to send HTTP requests to controllers in a test environment

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper; // used for mapping POJO <-> JSON

    @DisplayName("JUnit test for /create endpoint")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or the behaviour to test
        ResultActions response = mockMvc.perform(post("/api/employees", employee)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employee.getLastName()))
                .andExpect(jsonPath("$.email").value(employee.getEmail()));
    }

    @DisplayName("JUnit test for GET /employees endpoint")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = List.of(Employee.builder()
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane@doe.com")
                        .build(),
                Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@doe.com")
                        .build());
        given(employeeService.getAllEmployees()).willReturn(employeeList);
        //when - action or the behaviour to test
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeList)));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(employeeList.size()))
                .andExpect(jsonPath("$[0].firstName").value(employeeList.get(0).getFirstName()));

    }


    @DisplayName("JUnit test for getEmployeeById - positive scenario")
    @Test
    public void givenValidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        long id = 1L;
        Employee employee = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build();
        given(employeeService.getEmployeeById(id)).willReturn(Optional.of(employee));
        //when - action or the behaviour to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", id));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()));
    }


    @DisplayName("JUnit test for getEmployeeById - negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturn404Response() throws Exception {
        //given - precondition or setup
        long id = 1L;
        given(employeeService.getEmployeeById(id)).willReturn(Optional.empty());
        //when - action or the behaviour to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", id));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("JUnit test for updateEmployee - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given - precondition or setup
        long id = 1L;
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build();

        given(employeeService.getEmployeeById(id)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or the behaviour to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedEmployee.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedEmployee.getEmail()));
    }


    @DisplayName("JUnit test for updateEmployee - negative scenario")
    @Test
    public void givenInvalidUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //given - precondition or setup
        long id = 1L;
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build();

        given(employeeService.getEmployeeById(id)).willReturn(Optional.empty());
        //when - action or the behaviour to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("JUnit test for deleteEmployee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //given - precondition or setup
        long id = 1;
        willDoNothing().given(employeeService).deleteEmployee(id);
        //when - action or the behaviour to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}",id));
        //then - verify the output
        response.andExpect(status().isOk());
    }
}
