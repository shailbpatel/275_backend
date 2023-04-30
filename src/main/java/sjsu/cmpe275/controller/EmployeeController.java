package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sjsu.cmpe275.service.EmployeeService;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.service.ErrorResponse;

@RestController
//@Transactional
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    /**
     * Creates a new employee for the specified employer and returns a ResponseEntity with the created employee in JSON or XML format,
     * depending on the value of the "format" parameter. If the creation of the employee fails, a bad request response is returned.
     *
     * @param name       the name of the employee
     * @param email      the email address of the employee (optional)
     * @param title      the job title of the employee (optional)
     * @param street     the street address of the employee (optional)
     * @param city       the city of the employee (optional)
     * @param state      the state of the employee (optional)
     * @param zip        the zip code of the employee (optional)
     * @param managerId  the ID of the employee's manager (optional)
     * @param employerId the ID of the employer to whom the employee belongs
     * @param format     the format of the response (either "json" or "xml")
     * @return a ResponseEntity with the created employee in JSON or XML format, depending on the value of the "format" parameter
     * @throws ResponseStatusException if there is an error while creating the employee
     */
    @PostMapping(value = "/create/{employerId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createEmployee(
            @RequestParam("name") String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "zip", required = false) String zip,
            @RequestParam(value = "managerId", required = false) Long managerId,
            @PathVariable("employerId") String employerId,
            @RequestParam(value = "format", defaultValue = "json") String format) {

        try {
            Employee newEmployee = employeeService.createEmployee(name, email, title, street, city, state, zip, managerId, employerId);

            HttpHeaders headers = new HttpHeaders();
            if ("json".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_JSON);
            }
            if ("xml".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_XML);
            }
            return ResponseEntity.status(HttpStatus.OK).body(newEmployee);

        } catch (ResponseStatusException ex) {
            ErrorResponse response = new ErrorResponse(ex.getStatus().value(), ex.getReason());
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        } catch (Exception ex) {
            ErrorResponse response = new ErrorResponse(400, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        }
    }
}
