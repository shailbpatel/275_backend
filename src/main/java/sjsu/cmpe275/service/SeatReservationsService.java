package sjsu.cmpe275.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sjsu.cmpe275.entity.BulkReservations;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.entity.Employer;
import sjsu.cmpe275.entity.SeatReservations;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.EmployerRepository;
import sjsu.cmpe275.repository.SeatReservationsRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Transactional
public class SeatReservationsService {

    @Autowired
    private SeatReservationsRepository seatReservationsRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployerRepository employerRepository;
    private static final CsvMapper mapper = new CsvMapper();

    public void createSeatReservation(String employerId, long employeeId, LocalDate reservationDate, boolean isGTD) throws Exception {
        Employer employer = employerRepository.findById(employerId);
        if(employer == null) throw new Exception("Employer does not exist");

        Employee employee = employeeRepository.findByIdAndEmployerId(employeeId, employerId);
        if(employee == null) throw new Exception("Employee does not exist");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        int bookingThisWeek = 0;
        boolean employeeMopReached = false;
        LocalDate mondayThisWeek = reservationDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        while(!mondayThisWeek.isAfter(reservationDate)) {
            SeatReservations reservation = seatReservationsRepository.findByEmployeeIdAndEmployerIdAndReservationDate(employeeId, employerId, mondayThisWeek.format(formatter));
            if(reservation != null) bookingThisWeek++;
            mondayThisWeek = mondayThisWeek.plusDays(1);
        }
        if(bookingThisWeek >= employee.getMop()) employeeMopReached = true;

        List<SeatReservations> reservationsToday = seatReservationsRepository.findByEmployerIdAndReservationDate(employerId, reservationDate.format(formatter));
        if(reservationsToday.size() >= employer.getSeats()) {
            if(!employeeMopReached) {
                boolean bookedPreemptable = false;
                for(SeatReservations reservation: reservationsToday) {
                    if(!reservation.isPreemptable()) continue;
                    reservation.setEmployeeId(employeeId);
                    reservation.setPreemptable(false);
                    seatReservationsRepository.save(reservation);
                    bookedPreemptable = true;
                    break;
                }
                if(!bookedPreemptable)  throw new Exception("Employer seating capacity reached & no preemptable booking found, cannot book this seat");
            } else {
                throw new Exception("Employer seating capacity reached, cannot book this seat");
            }
        } else {
            SeatReservations newReservation = new SeatReservations(employerId, employeeId, reservationDate.format(formatter), isGTD, !isGTD && employeeMopReached);
            seatReservationsRepository.save(newReservation);
        }
    }

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

    public void convertToSeatReservations(String employerId, List<BulkReservations> seatReservations) throws Exception {
        for (BulkReservations reservation : seatReservations) {
            Employee employee = employeeRepository.findByEmployerIdAndEmail(employerId, reservation.getEmployeeEmailId());
            if (employee == null) {
                throw new RuntimeException("Employee does not exist");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDate startDate = LocalDate.parse(reservation.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(reservation.getEndDate(), formatter);
            while(!startDate.isAfter(endDate)) {
                createSeatReservation(employerId, employee.getId(), startDate, false);
                startDate = startDate.plusDays(1);
            }
        }
    }
}
