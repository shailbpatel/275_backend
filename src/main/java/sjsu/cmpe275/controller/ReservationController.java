package sjsu.cmpe275.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sjsu.cmpe275.entity.*;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.SeatReservationsRepository;
import sjsu.cmpe275.service.SeatReservationsService;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("")
public class ReservationController {

    @Autowired
    private SeatReservationsRepository seatReservationsRepository;

    @Autowired
    private final SeatReservationsService seatReservationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public ReservationController(SeatReservationsService seatReservationService) {
        this.seatReservationService = seatReservationService;
    }


    @PostMapping("/seatreservation")
    public ResponseEntity<?> makeSeatReservation(@RequestBody ReservationBody reservationBody) {


        SeatReservations savedReservation = seatReservationService.createSeatReservation(reservationBody.getEmployerId(), Long.valueOf(reservationBody.getEmployeeId()), reservationBody.getStartDate(), reservationBody.getEndDate(), reservationBody.isPreemptable(), reservationBody.isGTD());

        if (savedReservation != null) {
            return ResponseEntity.ok(savedReservation);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //Bulk Seat Reservation
    @PostMapping(value = "/bulk/reservation")
    public ResponseEntity<String> bulkReservations(@RequestParam("csvFile") MultipartFile file) throws IOException {

        try {

            if (!file.isEmpty()) {
                System.out.println("filed received");
            }

            List<BulkReservations> bulkReservations = seatReservationService.read(file.getInputStream(), BulkReservations.class);
            List<SeatReservations> reservations = seatReservationService.convertToSeatReservations(bulkReservations);

            return ResponseEntity.status(HttpStatus.OK).body(new String("Sucess"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new String("Failed"));
        }
    }


}
