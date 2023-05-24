package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sjsu.cmpe275.entity.BulkEmployee;
import sjsu.cmpe275.service.EmployeeService;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.service.ErrorResponse;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
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
    @PostMapping(value = "/create/{employerId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createEmployee(
            @RequestParam(value = "tokenId", required = false) String tokenId,
            @RequestParam(value = "is_google", required = false) boolean isGoogle,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "zip", required = false) String zip,
            @RequestParam(value = "manager_id", required = false) Long managerId,
            @RequestParam(value = "employer_id") String employerId,
            @RequestParam(value = "format", defaultValue = "json") String format) {

        try {
            Employee newEmployee = employeeService.createEmployee(name, email, password, title, street, city, state, zip, managerId, employerId, isGoogle);
            return ResponseEntity.status(HttpStatus.OK).body(newEmployee);
        } catch (ResponseStatusException ex) {
            ErrorResponse response = new ErrorResponse(ex.getStatus().value(), ex.getReason());
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        } catch (Exception ex) {
            ErrorResponse response = new ErrorResponse(400, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        }
    }


    @PostMapping("/{employerId}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable String employerId, @RequestParam("csvFile") MultipartFile file) throws IOException {
        List<BulkEmployee> bulkEmployees = employeeService.read(file.getInputStream(),BulkEmployee.class);
        List<Employee> employees = employeeService.convertToEmployees(employerId, bulkEmployees);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new String("Sucess"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new String("Failed"));
        }
    }
}
