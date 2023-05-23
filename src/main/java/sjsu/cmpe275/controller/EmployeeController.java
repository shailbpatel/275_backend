package sjsu.cmpe275.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sjsu.cmpe275.entity.Address;
import sjsu.cmpe275.entity.BulkEmployee;
import sjsu.cmpe275.entity.Employer;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.EmployerRepository;
import sjsu.cmpe275.service.EmployeeService;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.service.ErrorResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
//@Transactional
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


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


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("csvFile") MultipartFile file) throws IOException {

        if(!file.isEmpty()) {
            System.out.println("filed received");
        }

        System.out.println(file.getName());

        String content = new String(file.getBytes());

        List<BulkEmployee> bulkEmployees = this.read(file.getInputStream(),BulkEmployee.class);
        List<Employee> employees = convertToEmployees(bulkEmployees);



        System.out.println(bulkEmployees.toString());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new String("Sucess"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new String("Failed"));
        }
    }

    private static final CsvMapper mapper = new CsvMapper();
    public static <T> List<T> read(InputStream stream, Class<T> clazz) throws IOException {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("employeeEmailId")
                .addColumn("employeeName")
                .addColumn("password")
                .addColumn("managerEmailId")
                .build();
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        MappingIterator<T> iterator = reader.readValues(stream);
        return iterator.readAll();
    }

    private List<Employee> convertToEmployees(List<BulkEmployee> bulkEmployees) {
        List<Employee> employees = new ArrayList<>();
        for (BulkEmployee bulkEmployee : bulkEmployees) {
//            Employee employee = new Employee();

            // Set the values from BulkEmployee to Employee
//            employee.setEmail(bulkEmployee.getEmployeeEmailId());
//            employee.setName(bulkEmployee.getEmployeeName());
//            employee.setPassword(bulkEmployee.getPassword());
//            employee.setManagerEmployerId(bulkEmployee.getManagerEmailId());
//            employee.setEmployerId("SJSU");

            String employerId = "SJSU";


            Employer employer = employerRepository.findById(employerId);
            if (employer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employer object does not exist!");
            }
            Employee Manager = null;
            if(!bulkEmployee.getManagerEmailId().isEmpty()){

                Manager = employeeRepository.findByEmployerIdAndEmail(employerId, bulkEmployee.getManagerEmailId());
                if (Manager == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager does not exist!");
                }
            }
            long id = employeeService.generateEmployeeId(employerId);
            Employee employee = new Employee(id, employerId, bulkEmployee.getEmployeeName(), bulkEmployee.getEmployeeEmailId(), bulkEmployee.getPassword(), null, null, employer, Manager, false);
            employeeRepository.save(employee);
            employees.add(employee);

        }
        return employees;
    }
}
