package sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjsu.cmpe275.entity.SeatReservations;
import sjsu.cmpe275.repository.SeatReservationsRepository;

@Service
public class SeatReservationsService {

    @Autowired
    private SeatReservationsRepository seatReservationsRepository;

    public SeatReservations createSeatReservation(String employerId, Long employeeId, String startDate, String endDate, boolean isPreemptable, boolean isGTD) {

        SeatReservations newReservation = new SeatReservations(employerId, employeeId, startDate, endDate, isPreemptable, isGTD);

        newReservation.setEmployeeId(employeeId);
        newReservation.setEmployerId(employerId);
        newReservation.setStartDate(startDate);
        newReservation.setEndDate(endDate);
        newReservation.setPreemptable(isPreemptable);
        newReservation.setGTD(isGTD);
        return seatReservationsRepository.save(newReservation);

    }

}
