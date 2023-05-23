package sjsu.cmpe275.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sjsu.cmpe275.entity.SeatReservations;
import sjsu.cmpe275.repository.SeatReservationsRepository;

class seatReservationBody {
    String employeeId;
    String employerId;
    String startDate;
    String endDate;
    boolean isGTD;
    boolean isPreemptable;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isGTD() {
        return isGTD;
    }

    public void setGTD(boolean GTD) {
        isGTD = GTD;
    }

    public boolean isPreemptable() {
        return isPreemptable;
    }

    public void setPreemptable(boolean preemptable) {
        isPreemptable = preemptable;
    }
}

@RestController
@RequestMapping("")
public class ReservationController {


    @Autowired
    private  SeatReservationsRepository seatReservationsRepository;

    @Autowired
    public ReservationController(SeatReservationsRepository seatReservationsRepository) {
        this.seatReservationsRepository = seatReservationsRepository;
    }

    @PostMapping("/seatReservation")
    public ResponseEntity<?> makeSeatReservation(@RequestBody seatReservationBody seatReservation) {
        // Validate the request or perform any necessary checks
        SeatReservations newReservation = new SeatReservations(seatReservation.getEmployerId(), Long.valueOf(seatReservation.getEmployeeId()), seatReservation.isPreemptable(), seatReservation.isGTD());
        // Save the seat reservation to the database
        SeatReservations savedReservation =  seatReservationsRepository.save(newReservation);

        if (savedReservation != null) {
            // Return a success response with the saved reservation
            return ResponseEntity.ok(savedReservation);
        } else {
            // Return an error response if the reservation couldn't be saved
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
