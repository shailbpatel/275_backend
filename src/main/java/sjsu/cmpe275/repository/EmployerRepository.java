package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;

import sjsu.cmpe275.entity.Employer;

import java.util.List;
import java.util.Optional;

public interface EmployerRepository extends CrudRepository<Employer, Long> {
    Employer findByName(String name);

    List<Employer> findAll();

    Employer findById(String id);

    Employer findByEmail(String email);

    Employer findByToken(String token);
}