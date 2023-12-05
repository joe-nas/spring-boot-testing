package io.github.joenas.testingapp.repository;

import io.github.joenas.testingapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

//JpaRepository is internally annotated with @Repository, so we don't need to annotate EmployeeRepository with @Repository.
// All methods defined here are also annotated with @Transactional, so we don't need to annotate them with @Transactional.
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // define custom query usinf JPQL with index parameters

    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

}
