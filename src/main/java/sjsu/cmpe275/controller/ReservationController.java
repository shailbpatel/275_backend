package sjsu.cmpe275.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sjsu.cmpe275.entity.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.service.SeatReservationsService;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private final SeatReservationsService seatReservationService;
    @Autowired
    private final EmployeeRepository employeeRepository;

    public ReservationController(SeatReservationsService seatReservationService, EmployeeRepository employeeRepository) {
        this.seatReservationService = seatReservationService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/{employerId}")
    public ResponseEntity<?> makeSeatReservation(@PathVariable String employerId, @RequestBody SelfReservationBody reservationBody) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDate startDate = LocalDate.parse(reservationBody.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(reservationBody.getEndDate(), formatter);
        while(!startDate.isAfter(endDate)) {
            try {
                Employee employee = employeeRepository.findByEmployerIdAndEmail(employerId, reservationBody.getEmail());
                seatReservationService.createSeatReservation(employerId, employee.getId(), startDate, false);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            startDate = startDate.plusDays(1);
        }
        return ResponseEntity.ok("Reservation created successfully");
    }


    @PostMapping(value = "/bulk/{employerId}")
    public ResponseEntity<String> bulkReservations(@PathVariable String employerId, @RequestParam("csvFile") MultipartFile file) throws IOException {
        try {
            List<BulkReservations> bulkReservations = seatReservationService.read(file.getInputStream(), BulkReservations.class);
            seatReservationService.convertToSeatReservations(employerId, bulkReservations);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully created reservations");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed: " + e.getMessage());
        }
    }


}
