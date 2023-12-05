package io.github.joenas.testingapp.repository;

import io.github.joenas.testingapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//JpaRepository is internally annotated with @Repository, so we don't need to annotate EmployeeRepository with @Repository.
// All methods defined here are also annotated with @Transactional, so we don't need to annotate them with @Transactional.
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // define custom query using JPQL with index parameters
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    // define custom query using JPQL with named parameters
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // define custom query using SQL with index params
    @Query(value = "select * from employees e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String LastName);

    // define custom query using SQL with named params
    @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName", nativeQuery = true)
    Employee findByNativeSQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String LastName);
}
