package sjsu.cmpe275.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sjsu.cmpe275.service.CollaboratorService;
import sjsu.cmpe275.service.ErrorResponse;

@RestController
@RequestMapping(value = "/collaborators")
public class CollaboratorController {

    @Autowired
    private CollaboratorService collaboratorService;

    /**
     * Adds a collaborator between two employees of different employers.
     *
     * @param employerId1 the ID of the employer of the first employee
     * @param employeeId1 the ID of the first employee
     * @param employerId2 the ID of the employer of the second employee
     * @param employeeId2 the ID of the second employee
     * @return a ResponseEntity<String> with a success message if the collaboration was added
     * @throws Exception if there is an error while adding the collaborator
     */
    @PutMapping("/{employerId1}/{employeeId1}/{employerId2}/{employeeId2}")
    public ResponseEntity<?> addCollaborator(
            @PathVariable("employerId1") String employerId1,
            @PathVariable("employeeId1") long employeeId1,
            @PathVariable("employerId2") String employerId2,
            @PathVariable("employeeId2") long employeeId2) {

        try {
            Boolean success = collaboratorService.addCollaborator(employerId1, employeeId1, employerId2, employeeId2);
            return ResponseEntity.ok().body("Collaborator added successfully.");

        } catch (IllegalArgumentException e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        }
        catch (ResponseStatusException ex) {
            ErrorResponse response = new ErrorResponse(ex.getStatus().value(), ex.getReason());
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        }
    }

    /**
     * Deletes a collaboration between two employees identified by their employer IDs and employee IDs.
     *
     * @param employerId1 the employer ID of the first employee
     * @param employeeId1 the employee ID of the first employee
     * @param employerId2 the employer ID of the second employee
     * @param employeeId2 the employee ID of the second employee
     * @return a ResponseEntity with a success message if the collaboration is deleted successfully, or a not found message if either employee or collaboration does not exist
     * @throws Exception if there is an error deleting the collaboration
     */

    @DeleteMapping("/{employerId1}/{employeeId1}/{employerId2}/{employeeId2}")
    public ResponseEntity<String> deleteCollaborator(
            @PathVariable("employerId1") String employerId1,
            @PathVariable("employeeId1") long employeeId1,
            @PathVariable("employerId2") String employerId2,
            @PathVariable("employeeId2") long employeeId2) {

        try {
            Boolean success = collaboratorService.deleteCollaborator(employerId1, employeeId1, employerId2, employeeId2);
            if (!success)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee or Collaboration does not exists!");

            return ResponseEntity.ok().body("Collaborator deleted successfully.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
