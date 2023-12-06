package io.github.joenas.testingapp.service.impl;

import io.github.joenas.testingapp.exception.ResourceNotFoundException;
import io.github.joenas.testingapp.model.Employee;
import io.github.joenas.testingapp.repository.EmployeeRepository;
import io.github.joenas.testingapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    // given the EmployeeServiceImpl depends on EmployeeRepository one needs to Mock the Repository during testing
    // this happens using the Mockito library.
    EmployeeRepository employeeRepository;

    // constructor based dependency injection
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        //Store only employees with unique emails
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(savedEmployee.isPresent()){
            throw  new ResourceNotFoundException("Employee already exists with email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }


}
