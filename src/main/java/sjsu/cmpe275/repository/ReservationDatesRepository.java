package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;
import sjsu.cmpe275.entity.ReservationDates;


public interface ReservationDatesRepository extends CrudRepository<ReservationDates, Long> {

}
