package sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sjsu.cmpe275.repository.*;
import sjsu.cmpe275.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
//@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * Generates a new employee ID for a specified employer.
     *
     * @param employerId the ID of the employer for whom the new employee ID will be generated
     * @return a new employee ID that is unique within the specified employer's employee list
     */
    public long generateEmployeeId(String employerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Employee> root = cq.from(Employee.class);
        cq.select(cb.max(root.get("id"))).where(cb.equal(root.get("employerId"), employerId));
        Long maxId = entityManager.createQuery(cq).getSingleResult();
        return maxId == null ? 1 : maxId + 1;
    }


    /**
     * Creates a new Employee with the given name, email, title, street, city, state, zip, managerId, and employerId.
     *
     * @param name       the name of the Employee
     * @param email      the email address of the Employee
     * @param title      the job title of the Employee
     * @param street     the street address of the Employee
     * @param city       the city of the Employee's address
     * @param state      the state of the Employee's address
     * @param zip        the zip code of the Employee's address
     * @param managerId  the ID of the Employee's manager (nullable)
     * @param employerId the ID of the Employer associated with the Employee
     * @return the newly created Employee
     * @throws ResponseStatusException if the Employer object does not exist or the Manager does not exist
     */

    public Employee createEmployee(String name, String email, String title, String street, String city, String state, String zip, Long managerId, String employerId) {
        Employer optionalEmployer = employerRepository.findById(employerId);
        if (optionalEmployer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employer object does not exist!");
        }
        Employee Manager = null;
        if (managerId != null) {
            Manager = employeeRepository.findByIdAndEmployerId(managerId, employerId);
            if (Manager == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager does not exist!");
            }
        }
        Address address = new Address(street, city, state, zip);
        List<Employee> reports = new ArrayList<>();
        List<Employee> collaborators = new ArrayList<>();
        long id = generateEmployeeId(employerId);
        Employee employee = new Employee(id, employerId, name, email, title, address, optionalEmployer, Manager, collaborators, reports);

        if (Manager != null) {
            employee.setManager(Manager);
        }

        List<Employee> currentEmpList = optionalEmployer.getEmployees();
        if (currentEmpList == null) {
            currentEmpList = new ArrayList<Employee>();
        }
        currentEmpList.add(employee);
        optionalEmployer.setEmployees(currentEmpList);

        Employee savedEmployee = employeeRepository.save(employee);
        return savedEmployee;
    }


    /**
     * Retrieves an Employee object from the employee repository based on the provided EmployeeId object.
     *
     * @param employeeId the EmployeeId object containing the ID of the employee and the ID of their employer
     * @return the Employee object if found in the repository
     * @throws ResponseStatusException if an Employee object with the given IDs is not found in the repository
     */
    public Employee getEmployee(EmployeeId employeeId) throws ResponseStatusException {
        Employee optionalEmployee = employeeRepository.findByIdAndEmployerId(employeeId.getId(), employeeId.getEmployerId());
        if (optionalEmployee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for the given employer and employee ID.");
        }
        return optionalEmployee;
    }

    /**
     * Updates an existing employee with the provided details. If any of the fields are not provided, they will not be updated.
     *
     * @param employeeId the unique identifier of the employee to be updated, including their employer ID
     * @param name       the updated name of the employee, or null if the name should not be updated
     * @param email      the updated email of the employee, or null if the email should not be updated
     * @param title      the updated job title of the employee, or null if the job title should not be updated
     * @param street     the updated street address of the employee, or null if the address should not be updated
     * @param city       the updated city of the employee, or null if the address should not be updated
     * @param state      the updated state of the employee, or null if the address should not be updated
     * @param zip        the updated zip code of the employee, or null if the address should not be updated
     * @param managerId  the unique identifier of the new manager of the employee, or null if the employee should not have a manager
     * @return the updated employee object, with any fields that were updated
     * @throws ResponseStatusException if the employee or manager is not found, or if the manager is not from the same employer, or if the employee tries to set themselves as their own manager
     */
    @Transactional
    public Employee updateEmployee(EmployeeId employeeId, String name, String email, String title, String street, String city, String state, String zip, Long managerId) throws ResponseStatusException {
        Employee optionalEmployee = employeeRepository.findByIdAndEmployerId(employeeId.getId(), employeeId.getEmployerId());
        if (optionalEmployee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for the given employer and employee ID.");
        }
        Employee employee = optionalEmployee;
        employee.setName(name);
        employee.setEmail(email);
        employee.setTitle(title);

        Address address = employee.getAddress();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZip(zip);
        employee.setAddress(address);

        String managerEmployerId = employeeId.getEmployerId();
        if (managerId != null && managerEmployerId != null) {
            Employee newManager = employeeRepository.findByIdAndEmployerId(managerId, employeeId.getEmployerId());
            if (newManager == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager does not exist!");
            }
            if (newManager.equals(employee)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee cannot be their own manager!");
            }
            if (newManager == null && !newManager.getEmployer().equals(employee.getEmployer())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager is not from the same employer!");
            }
            Employee currentManager = employee.getManager();
            if(currentManager != null) {
                List<Employee> oldReports = currentManager.getReports();
                oldReports.add(employee);
                currentManager.setReports(oldReports);
            }

            List<Employee> currentReports = newManager.getReports();
            currentReports.add(employee);
            newManager.setReports(currentReports);
            employee.setManager(newManager);

        } else if (managerId == null && managerEmployerId == null) {
            employee.setManager(null);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        entityManager.flush();
        return savedEmployee;
    }

    /**
     * Deletes an Employee with the given ID and employer ID from the database, if found, and returns the deleted Employee object.
     *
     * @param id         The ID of the Employee to be deleted.
     * @param employerId The ID of the employer of the Employee to be deleted.
     * @return The deleted Employee object.
     * @throws ResponseStatusException If the Employee with the given ID and employer ID is not found, or if the Employee has reports and cannot be deleted.
     */
//    @Transactional
    public Employee deleteEmployee(Long id, String employerId) throws ResponseStatusException {
        Employee optionalEmployee = employeeRepository.findByIdAndEmployerId(id, employerId);
        if (optionalEmployee != null) {
            ;
            if (!optionalEmployee.getReports().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee has reports and cannot be deleted.");
            }
            Employee currentManager = optionalEmployee.getManager();
            if (currentManager != null) {
                List<Employee> oldReports = currentManager.getReports();
                oldReports.remove(optionalEmployee);
                currentManager.setReports(oldReports);
            }
            employeeRepository.delete(optionalEmployee);
            entityManager.flush();
            return optionalEmployee;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
    }
}
