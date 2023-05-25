package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;
import sjsu.cmpe275.entity.SeatReservations;

import java.util.List;

public interface SeatReservationsRepository extends CrudRepository<SeatReservations,Integer> {
    SeatReservations findByEmployeeIdAndEmployerIdAndReservationDate(long employeeId, String employerId, String reservationDate);

    List<SeatReservations> findByEmployerIdAndReservationDate(String employerId, String reservationDate);
}
