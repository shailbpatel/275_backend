package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import sjsu.cmpe275.service.GTDService;

@RestController
@RequestMapping("/gtd")
public class GTDController {

    @Autowired
    private GTDService gtdService;

    @GetMapping("/{employerId}/{employeeEmail}")
    public ResponseEntity<?> getGTD(@PathVariable String employerId, @PathVariable String employeeEmail) {
        try {
            String gtd = gtdService.getGtd(employerId, employeeEmail);
            return ResponseEntity.ok(gtd);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{employerId}/{employeeEmail}/{newGtd}")
    public ResponseEntity<String> setGTD(@PathVariable String employerId, @PathVariable String employeeEmail, @PathVariable int newGtd) {
        try {
            gtdService.setGtd(employerId, employeeEmail, newGtd);
            return ResponseEntity.ok("GTD successfully set");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{employerId}/{employeeEmail}")
    public ResponseEntity<String> deleteGtd(@PathVariable String employerId, @PathVariable String employeeEmail) {
        try {
            gtdService.deleteGtd(employerId, employeeEmail);
            return ResponseEntity.ok("GTD successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
