package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sjsu.cmpe275.entity.*;
import sjsu.cmpe275.repository.DirectReportRepository;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.EmployerRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/mop")
public class MOPController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private DirectReportRepository directReportRepository;

    private void updateMOP(User userObj, int newMOP) {
        if(userObj.getClass() == Employer.class) {
            List<Employee> allEmployees = employeeRepository.findByEmployerId((String) userObj.getEmployer().getId());
            for(Employee employee: allEmployees) {
                employee.setMop(Math.max(newMOP, employee.getMop()));
                employeeRepository.save(employee);
            }
        } else {
            Queue<Employee> queue = new LinkedList<>();
            List<DirectReport> reports = directReportRepository.findByEmployerIdAndManagerId((String) userObj.getEmployer().getId(), (long) userObj.getId());
            for(DirectReport report: reports) {
                Employee employee = employeeRepository.findByIdAndEmployerId(report.getReportId(), report.getEmployerId());
                queue.add(employee);
            }
            while(!queue.isEmpty()) {
                Employee empObj = queue.poll();
                List<DirectReport> newReports = directReportRepository.findByEmployerIdAndManagerId((String) empObj.getEmployer().getId(), (long) empObj.getId());
                for(DirectReport report: newReports) {
                    Employee employee = employeeRepository.findByIdAndEmployerId(report.getReportId(), report.getEmployerId());
                    queue.add(employee);
                }
                empObj.setMop(Math.max(newMOP, empObj.getMop()));
                employeeRepository.save(empObj);
            }
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getMOP(@RequestParam String employerId, @RequestParam String email) {
        User user;
        if(!email.equals("")) {
            user = employeeRepository.findByEmployerIdAndEmail(employerId, email);
        }
        else {
            user = employerRepository.findById(employerId);
        }
        if(user != null)  return ResponseEntity.status(HttpStatus.OK).body(user.getMop());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
    }

    @PostMapping("")
    public ResponseEntity<?> setMOP(@RequestBody MOPRequestBody requestBody, HttpServletRequest request) {
        User userObj;
        if(requestBody.getRole().equals("Employer")) {
            userObj = employerRepository.findByEmail(requestBody.getEmail());
        } else {
            userObj = employeeRepository.findByEmployerIdAndEmail(requestBody.getEmployerId(), requestBody.getEmail());
        }
        if(userObj == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        if(requestBody.getRole().equals("Employee")) {
            DirectReport immediateManagerMapping = directReportRepository.findByEmployerIdAndReportId(requestBody.getEmployerId(), (long) userObj.getId());
            if(immediateManagerMapping != null) {
                Employee immediateManager = employeeRepository.findByIdAndEmployerId(immediateManagerMapping.getManagerId(), immediateManagerMapping.getEmployerId());
                userObj.setMop(Math.max(immediateManager.getMop(), Math.max(requestBody.getMop(), userObj.getEmployer().getMop())));
            }
            else userObj.setMop(Math.max(requestBody.getMop(), userObj.getEmployer().getMop()));
        }
        else {
            userObj.setMop(requestBody.getMop());
        }

        if(userObj.getClass() == Employee.class) {
            employeeRepository.save((Employee) userObj);
        } else {
            employerRepository.save((Employer) userObj);
        }
        updateMOP(userObj, Math.max(requestBody.getMop(), userObj.getEmployer().getMop()));
        return ResponseEntity.status(HttpStatus.OK).body("MOP updated successfully");
    }

    // API for deleting MOP
    @DeleteMapping("")
    public ResponseEntity<?> deleteMOP(@RequestBody MOPRequestBody requestBody, HttpServletRequest request) {
        User userObj;
        if(requestBody.getRole().equals("Employer")) {
            userObj = employerRepository.findByEmail(requestBody.getEmail());
        } else {
            userObj = employeeRepository.findByEmployerIdAndEmail(requestBody.getEmployerId(), requestBody.getEmail());
        }
        if(userObj == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        if(requestBody.getRole().equals("Employee")) {
            DirectReport immediateManagerMapping = directReportRepository.findByEmployerIdAndReportId(requestBody.getEmployerId(), (long) userObj.getId());
            Employee immediateManager = employeeRepository.findByIdAndEmployerId(immediateManagerMapping.getManagerId(), immediateManagerMapping.getEmployerId());
            userObj.setMop(immediateManager.getMop());
            updateMOP(userObj, immediateManager.getMop());
        }
        else {
            userObj.setMop(0);
            updateMOP(userObj, 0);
        }
        if(userObj.getClass() == Employee.class) {
            employeeRepository.save((Employee) userObj);
        } else {
            employerRepository.save((Employer) userObj);
        }
        return ResponseEntity.status(HttpStatus.OK).body("MOP updated successfully");
    }
}
