package sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjsu.cmpe275.repository.EmployeeRepository;

@Service
public class ComplianceRateService {

    @Autowired

    private final EmployeeRepository employeeRepository;


    public ComplianceRateService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //number of employees meeting mop

}
