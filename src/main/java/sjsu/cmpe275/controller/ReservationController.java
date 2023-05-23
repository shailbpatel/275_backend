package sjsu.cmpe275.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sjsu.cmpe275.entity.ReservationBody;
import sjsu.cmpe275.entity.SeatReservations;
import sjsu.cmpe275.repository.SeatReservationsRepository;
import sjsu.cmpe275.service.SeatReservationsService;



@RestController
@RequestMapping("")
public class ReservationController {


    @Autowired
    private  SeatReservationsRepository seatReservationsRepository;


    @Autowired
    private final SeatReservationsService seatReservationService;

    public ReservationController(SeatReservationsService seatReservationService) {
        this.seatReservationService = seatReservationService;
    }


    @PostMapping("/seatReservation")
    public ResponseEntity<?> makeSeatReservation(@RequestBody ReservationBody reservationBody) {


        // Validate the request or perform any necessary checks
        SeatReservations savedReservation = seatReservationService.createSeatReservation(reservationBody.getEmployerId(), Long.valueOf(reservationBody.getEmployeeId()), reservationBody.getStartDate(), reservationBody.getEndDate(), reservationBody.isPreemptable(), reservationBody.isGTD());

        if (savedReservation != null) {
            // Return a success response with the saved reservation
            return ResponseEntity.ok(savedReservation);
        } else {
            // Return an error response if the reservation couldn't be saved
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
