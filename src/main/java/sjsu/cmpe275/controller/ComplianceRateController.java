package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sjsu.cmpe275.entity.ReservationDates;
import sjsu.cmpe275.repository.EmployeeRepository;

@RestController
@CrossOrigin
public class ComplianceRateController {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public ComplianceRateController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/compliancerate")
    public ResponseEntity<String> complianceRate(@RequestBody  ReservationDates reservationDates){
        try {
//            attendanceMeetRateService.getAllReservationDatesCounts(reservationDates.getStartDate(), reservationDates.getEndDate());
            return ResponseEntity.status(HttpStatus.OK).body("Sucess Compliance Rate");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Failed");
        }



    }
}
