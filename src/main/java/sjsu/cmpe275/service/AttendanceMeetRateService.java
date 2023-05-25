package sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjsu.cmpe275.entity.Employee;
import sjsu.cmpe275.entity.SeatReservations;
import sjsu.cmpe275.repository.EmployeeRepository;
import sjsu.cmpe275.repository.ReservationDatesRepository;
import sjsu.cmpe275.repository.SeatReservationsRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceMeetRateService {

    @Autowired
    private final ReservationDatesRepository reservationDatesRepository;

    @Autowired
    private final SeatReservationsRepository seatReservationsRepository;

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeeService employeeService;

    public AttendanceMeetRateService(ReservationDatesRepository reservationDatesRepository, SeatReservationsRepository seatReservationsRepository, EmployeeRepository employeeRepository, EmployerService employerService, EmployeeService employeeService) {
        this.reservationDatesRepository = reservationDatesRepository;
        this.seatReservationsRepository = seatReservationsRepository;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    //Getting reservation counts
    public int getAllReservationDatesCounts(String startDate, String endDate) {
        if (startDate != null && endDate != null) {}

            List<SeatReservations> seatReservations = (List<SeatReservations>) seatReservationsRepository.findAll();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate receivedStartDate = LocalDate.parse(startDate, dateFormatter);
            LocalDate receivedEndDate = LocalDate.parse(endDate, dateFormatter);


            List<SeatReservations> reservationsWithInWeek = new ArrayList<>();


            for (SeatReservations seatReservation : seatReservations) {

                Employee employee = employeeRepository.findByIdAndEmployerId(seatReservation.getEmployeeId(), seatReservation.getEmployerId());

                LocalDate currentStartDate = LocalDate.parse(seatReservation.getStartDate(), dateFormatter);
                LocalDate currentEndDate = LocalDate.parse(seatReservation.getEndDate(), dateFormatter);

                if ( (currentStartDate.isEqual(receivedStartDate) || currentStartDate.isAfter(receivedStartDate)) &&
                        (currentStartDate.isEqual(receivedEndDate) || currentStartDate.isBefore(receivedEndDate)) &&
                        (currentEndDate.isEqual(receivedStartDate) || currentEndDate.isAfter(receivedStartDate)) &&
                        (currentEndDate.isEqual(receivedEndDate) || currentEndDate.isBefore(receivedEndDate))) {

                    reservationsWithInWeek.add(seatReservation);

                }
            }

            return reservationsWithInWeek.size();
        }

}
