package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import sjsu.cmpe275.entity.Employer;
import sjsu.cmpe275.entity.SeatCapacity;
import sjsu.cmpe275.repository.EmployerRepository;

@RestController
@RequestMapping("/seats")
public class SeatCapacityController {
    @Autowired
    EmployerRepository employerRepository;

    @GetMapping("/{employerId}")
    public ResponseEntity<?> getSeatCapacity(@PathVariable String employerId) {
        Employer employer = employerRepository.findById(employerId);
        if(employer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        return  ResponseEntity.ok().body(employer.getSeats());
    }

    @PostMapping("/{employerId}")
    public ResponseEntity<?> changeSeatCapacity(@PathVariable String employerId, @RequestBody SeatCapacity requestBody) {
        Employer employer = employerRepository.findById(employerId);
        if(employer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        int oldCapacity = employer.getSeats();
        if(oldCapacity > requestBody.getSeats()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seat capacity cannot be reduced");
        employer.setSeats(requestBody.getSeats());
        employerRepository.save(employer);

        return ResponseEntity.ok().body("New seat capacity updated");
    }

}
