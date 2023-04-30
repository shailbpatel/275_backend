package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;

import sjsu.cmpe275.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Employee findByIdAndEmployerId(long id, String employer_id);
}