package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;
import sjsu.cmpe275.entity.EmployeeId;
import sjsu.cmpe275.entity.ManagerRequirements;

public interface ManagerRequirementsRepository extends CrudRepository<ManagerRequirements, EmployeeId> {
    ManagerRequirements findByIdAndEmployerId(Long id, String employerId);
}
