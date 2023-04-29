package sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.repository.EmployeeRepository;

@Service
public class CollaboratorService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Adds two employees as collaborators. This method first checks if the given employee IDs are valid and belong to the
     *      corresponding employer IDs. It then checks if the two employees are already collaborators. If they are, it returns
     *      true without making any changes. Otherwise, it adds each employee to the other's collaborators list and saves the
     *      changes to the database.
     *      @param employerId1 the ID of the employer of the first employee
     *      @param employeeId1 the ID of the first employee
     *      @param employerId2 the ID of the employer of the second employee
     *      @param employeeId2 the ID of the second employee
     *      @return true if the employees were successfully added as collaborators, false otherwise
     *      @throws IllegalArgumentException if employee1 is the same as employee2
     */
    public Boolean addCollaborator(long employerId1, long employeeId1, long employerId2, long employeeId2) {
        if (employerId1 == employerId2 && employeeId1 == employeeId2) {
            throw new IllegalArgumentException("Cannot add an employee as a collaborator of himself/herself.");
        }

        Employee employee1 = employeeRepository.findByIdAndEmployerId(employeeId1, employerId1);
        Employee employee2 = employeeRepository.findByIdAndEmployerId(employeeId2, employerId2);

        if (employee1 == null || employee2 == null ) {
            return false;
        }
        if(employee1.getCollaborators().contains(employee2)) {
            return true;
        }

        employee1.getCollaborators().add(employee2);
        employee2.getCollaborators().add(employee1);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        return true;
    }


    /**
     * Deletes the collaboration between two employees from different employers.
     * @param employerId1 the ID of the first employer
     * @param employeeId1 the ID of the first employee
     * @param employerId2 the ID of the second employer
     * @param employeeId2 the ID of the second employee
     * @return true if the collaboration was deleted successfully, false if either employee could not be found
     * @throws Exception if the two employees are the same or if the collaboration does not exist
     */
    public Boolean deleteCollaborator(long employerId1, long employeeId1, long employerId2, long employeeId2) throws Exception {
        if (employerId1 == employerId2 && employeeId1 == employeeId2) {
            throw new Exception("Cannot remove an employee as a collaborator of himself/herself.");
        }

        Employee employee1 = employeeRepository.findByIdAndEmployerId(employeeId1, employerId1);
        Employee employee2 = employeeRepository.findByIdAndEmployerId(employeeId2, employerId2);

        if (employee1 == null || employee2 == null ) {
            return false;
        }
        if(!employee1.getCollaborators().contains(employee2)) {
            throw new Exception("Collaboration does not exist.");
        }

        employee1.getCollaborators().remove(employee2);
        employee2.getCollaborators().remove(employee1);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        return true;
    }
}
