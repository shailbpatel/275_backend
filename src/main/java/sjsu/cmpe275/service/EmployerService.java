package sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import sjsu.cmpe275.entity.Address;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.entity.Employer;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.EmployerRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class EmployerService {
    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmployeeRepository employeeRepository;


    @Transactional
    public Employer createEmployer(String id, String name, String street, String city, String state, String zip, String email, String password, Integer seats, Boolean is_google) {
        if (employerRepository.findById(id) != null) {
            return null;
        }
        Address address = new Address(street, city, state, zip);
        Employer employer = new Employer(id, name, address, email, password, seats, is_google, false);
        Employer savedEmployer = employerRepository.save(employer);
        entityManager.flush();
        return savedEmployer;
    }

    /**
     * Retrieves the Employer with the specified ID from the database.
     *
     * @param id the ID of the Employer to retrieve
     * @return the Employer with the specified ID, or null if the Employer was not found in the database
     */
    public Employer getEmployer(String id) {
        Employer employer = employerRepository.findById(id);
        return employer;
    }

    /**
     * Returns an Iterable of all Employers in the database.
     *
     * @return an Iterable of all Employers in the database
     */
    public List<Employer> getAllEmployers() {
        return employerRepository.findAll();
    }

    /**
     * Updates the specified Employer in the database with the provided information.
     *
     * @param employerId  the ID of the Employer to update
     * @param name        the new name for the Employer (null to keep the existing name)
     * @param street      the new street address for the Employer's location (null to keep the existing address)
     * @param city        the new city for the Employer's location (null to keep the existing address)
     * @param state       the new state for the Employer's location (null to keep the existing address)
     * @param zip         the new zip code for the Employer's location (null to keep the existing address)
     * @return the updated Employer
     */
    @Transactional
    public Employer updateEmployer(String employerId, String name, String street, String city, String state, String zip) throws Exception {
        Employer optionalEmployer = employerRepository.findById(employerId);
        if (optionalEmployer == null) {
            throw new Exception("Employer does not exist!");
        }

        if (name != null) {
            optionalEmployer.setName(name);
        }

        if (street != null || city != null || state != null || zip != null) {
            Address address = optionalEmployer.getAddress();
            if (address == null) {
                address = new Address();
                optionalEmployer.setAddress(address);
            }
            if (street != null) {
                address.setStreet(street);
            }
            if (city != null) {
                address.setCity(city);
            }
            if (state != null) {
                address.setState(state);
            }
            if (zip != null) {
                address.setZip(zip);
            }
        }

        Employer savedEmployer = employerRepository.save(optionalEmployer);
        entityManager.flush();
        return savedEmployer;
    }

    /**
     * Deletes the Employer with the specified ID from the database, if it exists and has no associated Employees.
     *
     * @param id the ID of the Employer to delete
     * @return the deleted Employer, or null if the Employer was not found
     * @throws ResponseStatusException if the Employer with the specified ID has associated Employees
     */
    public Employer deleteEmployer(String id) {
        Employer optionalEmployer = employerRepository.findById(id);
        if (optionalEmployer != null) {
            List<Employee> empList = employeeRepository.findByEmployerId(id);
            if (empList != null) {
                if (!empList.isEmpty())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employer has employees and cannot be deleted.");
            }
            employerRepository.delete(optionalEmployer);
            return optionalEmployer;
        } else {
            return null;
        }
    }
}
