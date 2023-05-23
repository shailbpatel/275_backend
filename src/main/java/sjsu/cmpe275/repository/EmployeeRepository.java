package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;

import sjsu.cmpe275.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Employee findByIdAndEmployerId(long id, String employer_id);

    List<Employee> findByEmployerId(String employer_id);

    Employee findByEmployerIdAndEmail(String email, String employer_id);

    Employee findByEmail(String email);

    Employee findByToken(String token);
}