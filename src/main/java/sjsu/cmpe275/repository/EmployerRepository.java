package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;

import sjsu.cmpe275.entity.Employer;

import java.util.Optional;

public interface EmployerRepository extends CrudRepository<Employer, Long> {
    Employer findByName(String name);


    Employer findById(String id);
}