package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sjsu.cmpe275.entity.ReservationDates;
import sjsu.cmpe275.service.AttendanceMeetRateService;

@RestController
@CrossOrigin
public class AttendanceMeetRateController {

    @Autowired
    private AttendanceMeetRateService attendanceMeetRateService;

    @PostMapping(value = "/reservations/dates")
    public ResponseEntity<String> dates(@RequestBody ReservationDates reservationDates) {
        try {
            attendanceMeetRateService.getAllReservationDatesCounts(reservationDates.getStartDate(), reservationDates.getEndDate());
            return ResponseEntity.status(HttpStatus.OK).body("Sucess");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Failed");
        }
    }
}
