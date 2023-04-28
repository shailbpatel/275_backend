package sjsu.cmpe275.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.entity.EmployeeId;
import sjsu.cmpe275.entity.Employer;
import sjsu.cmpe275.service.EmployeeService;
import sjsu.cmpe275.service.EmployerService;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Creates a new employer and returns the corresponding HTTP response with the employer information.
     *
     * @param name        the name of the employer (required)
     * @param description the description of the employer (optional)
     * @param street      the street address of the employer (optional)
     * @param city        the city of the employer (optional)
     * @param state       the state of the employer (optional)
     * @param zip         the zip code of the employer (optional)
     * @param format      the format of the HTTP response, either "json" or "xml" (default is "json")
     * @return an HTTP response with the newly created employer information in the specified format
     * @throws ResponseStatusException if there was an error creating the employer
     */

    @CrossOrigin
    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Employer> createEmployer(
            @RequestParam(required = true) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String zip,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String capacity,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String password,
            @RequestParam(value = "format", defaultValue = "json") String format) throws ResponseStatusException {

        Employer newEmployer = employerService.createEmployer(name, description , street, city, password, state, zip, capacity, email, role);

        if (newEmployer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if ("json".equalsIgnoreCase(format)) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(newEmployer);
        } else if ("xml".equalsIgnoreCase(format)) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(newEmployer);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Retrieves an employer by ID and returns it in either JSON or XML format.
     *
     * @param employerId the ID of the employer to retrieve
     * @param format     the format of the response, defaults to "json"
     * @return a ResponseEntity containing the requested employer in either JSON or XML format, along with an HTTP status code of 200 (OK)
     * @throws ResponseStatusException if the employer with the given ID could not be found
     */
    @GetMapping(value = "/{employerId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Employer> getEmployer(@PathVariable Long employerId, @RequestParam(value = "format", defaultValue = "json") String format) throws ResponseStatusException {
        Employer employer = employerService.getEmployer(employerId);
        if (format.equals("xml")) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(employer);
        } else {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(employer);
        }
    }

    /**
     * Updates an existing employer with the given employer ID and other parameters, such as name, description, street, city, state, and zip. The employer is returned in JSON or XML format, depending on the "format" parameter.
     *
     * @param employerId  the ID of the employer to update
     * @param name        the new name of the employer
     * @param description the new description of the employer (optional)
     * @param street      the new street address of the employer (optional)
     * @param city        the new city of the employer (optional)
     * @param state       the new state of the employer (optional)
     * @param zip         the new zip code of the employer (optional)
     * @param format      the desired output format, either "json" or "xml" (optional)
     * @return a ResponseEntity containing the updated employer in JSON or XML format, or an error status if the employer
     * does not exist or if there is a bad request
     */
    @PutMapping(path = "/{employerId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Employer> updateEmployer(@PathVariable("employerId") long employerId,
                                                   @RequestParam(required = true) String name,
                                                   @RequestParam(required = false) String description,
                                                   @RequestParam(required = false) String street,
                                                   @RequestParam(required = false) String city,
                                                   @RequestParam(required = false) String state,
                                                   @RequestParam(required = false) String zip,
                                                   @RequestParam(required = false) String email,
                                                   @RequestParam(required = false) String capacity,
                                                   @RequestParam(required = false) String role,
                                                   @RequestParam(required = false) String password,
                                                   @RequestParam(required = false) String format) {

        try {
            Employer employer = employerService.updateEmployer(employerId, name, description, street, city, state, zip, capacity, email, role, password);
            if (format != null && format.equals("xml")) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(employer);
            } else {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(employer);
            }
        } catch (Exception e) {
            if (e.getMessage() == "Employer does not exist!") {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Deletes an employer with the specified employer ID.
     *
     * @param employerId the ID of the employer to be deleted
     * @param format     (optional) the desired format of the response (XML or JSON)
     * @return a ResponseEntity with a status code indicating success or failure, and a body containing the deleted employer object in the specified format (XML or JSON)
     * @throws Exception if there is an error while deleting the employer
     */


    @DeleteMapping(value = "/{employerId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteEmployer(@PathVariable("employerId") Long employerId, @RequestParam(required = false) String format) throws Exception {
        Employer deletedEmployer = employerService.deleteEmployer(employerId);

        if (deletedEmployer == null) {
            return ResponseEntity.notFound().build();
        }
        if (format != null && format.equals("xml")) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(deletedEmployer);
        } else {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(deletedEmployer);
        }
    }


    /**
     * Retrieves an Employee with the given ID from the specified employer, and returns it as either JSON or XML.
     *
     * @param employerId the ID of the employer that the Employee belongs to
     * @param id         the ID of the Employee to retrieve
     * @param format     the format to return the Employee as (either "json" or "xml"; defaults to "json" if not specified)
     * @return a ResponseEntity containing the retrieved Employee, with the appropriate content type set based on the format parameter
     * @throws ResponseStatusException if no Employee with the specified ID exists or if there is an error retrieving the Employee
     */

    @GetMapping(value = "/{employerId}/employee/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employerId, @PathVariable Long id, @RequestParam(value = "format", defaultValue = "json") String format) throws ResponseStatusException {
        EmployeeId employeeId = new EmployeeId(id, employerId);
        Employee employee = employeeService.getEmployee(employeeId);
        if (format.equals("xml")) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(employee);
        } else {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(employee);
        }
    }

    /**
     * Updates an employee with the given ID and employer ID.
     *
     * @param employerId the ID of the employer
     * @param id         the ID of the employee to update
     * @param name       the new name of the employee
     * @param email      the new email address of the employee (optional)
     * @param title      the new job title of the employee (optional)
     * @param street     the new street address of the employee (optional)
     * @param city       the new city of the employee (optional)
     * @param state      the new state of the employee (optional)
     * @param zip        the new zip code of the employee (optional)
     * @param managerId  the ID of the employee's new manager (optional)
     * @param format     the desired response format (optional, default is JSON)
     * @return a ResponseEntity containing the updated employee in the requested format
     * @throws ResponseStatusException if the employee or employer ID is invalid
     */
    @PutMapping(path = "/{employerId}/employee/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Employee> updateEmployee(@PathVariable("employerId") long employerId,
                                                   @PathVariable("id") long id,
                                                   @RequestParam(required = true) String name,
                                                   @RequestParam(required = false) String email,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String street,
                                                   @RequestParam(required = false) String city,
                                                   @RequestParam(required = false) String state,
                                                   @RequestParam(required = false) String zip,
                                                   @RequestParam(required = false) Long managerId,
                                                   @RequestParam(required = false) String format) throws ResponseStatusException {

        Employee employee = employeeService.updateEmployee(new EmployeeId(id, employerId), name, email, title, street, city, state, zip, managerId);
        if (format != null && format.equals("xml")) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(employee);
        } else {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(employee);
        }
    }

    /**
     * Deletes an employee from the database for a specific employer.
     *
     * @param employerId the ID of the employer for whom the employee is being deleted
     * @param id         the ID of the employee being deleted
     * @param format     the desired response format (optional)
     * @return a ResponseEntity with a status code and the deleted employee in either JSON or XML format
     * @throws ResponseStatusException if the employer or employee with the given IDs cannot be found in the database
     */
    @DeleteMapping(value = "/{employerId}/employee/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteEmployee(@PathVariable("employerId") Long employerId, @PathVariable("id") Long id, @RequestParam(required = false) String format) throws ResponseStatusException {
        Employee deletedEmployee = employeeService.deleteEmployee(id, employerId);
        if (format != null && format.equals("xml")) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(deletedEmployee);
        } else {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(deletedEmployee);
        }
    }

    /**
     * Handles a {@link ResponseStatusException} and returns a {@link ResponseEntity} with the status code and reason.
     *
     * @param ex the {@link ResponseStatusException} to handle
     * @return a {@link ResponseEntity} with the status code and reason
     */

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ex.getReason());
    }
}
