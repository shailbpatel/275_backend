package sjsu.cmpe275.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sjsu.cmpe275.entity.BulkReservations;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.entity.SeatReservations;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.SeatReservationsRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SeatReservationsService {

    @Autowired
    private SeatReservationsRepository seatReservationsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

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

    private static final CsvMapper mapper = new CsvMapper();

    public static <T> List<T> read(InputStream stream, Class<T> clazz) throws IOException {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("employeeEmailId")
                .addColumn("startDate")
                .addColumn("endDate")
                .build();
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        MappingIterator<T> iterator = reader.readValues(stream);
        return iterator.readAll();
    }

    public List<SeatReservations> convertToSeatReservations(List<BulkReservations> seatReservations) {
        List<SeatReservations> reservations = new ArrayList<>();
        try {


            for (BulkReservations reservation : seatReservations) {

                Employee employee = employeeRepository.findByEmail(reservation.getEmployeeEmailId());

                if (employee == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee is not registered");
                }

                if (employee.getEmployerId() == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employer not found for requested employee");
                }

                long employeeId = employee.getId();
                String employerId = employee.getEmployerId();

                SeatReservations seatreservation = new SeatReservations(employerId, employeeId, reservation.getStartDate(), reservation.getEndDate(), false, false);
                seatreservation.setEmployeeId(employeeId);
                seatreservation.setEmployerId(employerId);
                seatreservation.setStartDate(reservation.getStartDate());
                seatreservation.setEndDate(reservation.getEndDate());
                seatreservation.setPreemptable(false);
                seatreservation.setGTD(false);
                reservations.add(seatreservation);
                seatReservationsRepository.save(seatreservation);

            }
        } catch (Exception e) {

        }
        return reservations;
    }

}
