package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;
import sjsu.cmpe275.entity.SeatReservations;

public interface SeatReservationsRepository extends CrudRepository<SeatReservations,Integer> {


}
