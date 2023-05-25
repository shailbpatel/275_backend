package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.entity.Employer;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.EmployerRepository;

@RestController
@CrossOrigin
@RequestMapping("/")
public class VerificationController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("verify/{token}")
    public ResponseEntity<?> verifyUser(@PathVariable String token) {
        Employer employer = employerRepository.findByToken(token);
        if (employer != null) {
            employer.setIsVerified(true);
            employerRepository.save(employer);
            return ResponseEntity.ok("Account verified successfully");
        }

        Employee employee = employeeRepository.findByToken(token);
        if (employee != null){
            employee.setVerified(true);
            employeeRepository.save(employee);
            return ResponseEntity.ok("Account verified successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token");
    }
}
