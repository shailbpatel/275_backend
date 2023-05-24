package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.entity.Employer;
import sjsu.cmpe275.entity.UserLoginRequest;
import sjsu.cmpe275.entity.UserDetailsResponse;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.EmployerRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        String userType = "";
        Employee employeeUser = employeeRepository.findByEmail(userLoginRequest.getEmail());;
        Employer employerUser = employerRepository.findByEmail(userLoginRequest.getEmail());
        if(employerUser == null && employeeUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
        else if(employerUser != null) {
            userType = "Employer";
        }
        else {
            userType = "Employee";
        }

        if((employerUser!=null && userLoginRequest.getIsGoogle()!=employerUser.isIsGoogle()) ||
                (employeeUser!=null && userLoginRequest.getIsGoogle()!=employeeUser.isGoogle())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Use password for singing in");
        }
        if (!userLoginRequest.getIsGoogle()) {
            if((employerUser!=null && userLoginRequest.getPassword()!=null && employerUser.isIsGoogle()) ||
                (employeeUser!=null && userLoginRequest.getPassword()!=null && employeeUser.isGoogle())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Use Google for signing in");
            }

            if((employerUser == null || !employerUser.getPassword().equals(userLoginRequest.getPassword())) &&
                (employeeUser == null || !employeeUser.getPassword().equals(userLoginRequest.getPassword()))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password");
            }
        }
        if((employerUser != null && !employerUser.isIsVerified()) || (employeeUser != null && !employeeUser.isVerified())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not verified. Please verify using email");
        }
        // if authentication is successful, create a new session
        HttpSession session = request.getSession(true);
        session.setAttribute("useType", userType);
        session.setAttribute("employer", employerUser);
        session.setAttribute("employee", employeeUser);


        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        if(userType.equals("Employer")) {
            userDetailsResponse.setName(employerUser.getName());
            userDetailsResponse.setEmail(employerUser.getEmail());
            userDetailsResponse.setRole(userType);
            userDetailsResponse.setIsVerified(employerUser.isIsVerified());
            userDetailsResponse.setIsGoogle(employerUser.isIsGoogle());
            userDetailsResponse.setEmployerId(employerUser.getId());
        } else {
            userDetailsResponse.setName(employeeUser.getName());
            userDetailsResponse.setEmail(employeeUser.getEmail());
            userDetailsResponse.setRole(userType);
            userDetailsResponse.setIsVerified(employeeUser.isVerified());
            userDetailsResponse.setIsGoogle(employeeUser.isGoogle());
            userDetailsResponse.setEmployerId(employeeUser.getEmployer().getId());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("session_key", session.getId());
        response.put("user", userDetailsResponse);
        return ResponseEntity.ok(response);
    }
}
