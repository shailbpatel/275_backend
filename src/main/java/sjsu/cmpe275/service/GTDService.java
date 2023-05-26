package sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjsu.cmpe275.entity.*;
import sjsu.cmpe275.repository.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GTDService {
    @Autowired
    ManagerRequirementsRepository gtdRepository;
    @Autowired
    SeatReservationsService seatReservationsService;
    @Autowired
    SeatReservationsRepository seatReservationsRepository;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    DirectReportRepository directReportRepository;

    public String getGtd(String employerId, String employeeEmail) throws Exception {
        Employee employee = employeeRepository.findByEmployerIdAndEmail(employerId, employeeEmail);
        if(employee == null) throw new Exception("Employee not found");
        ManagerRequirements gtd = gtdRepository.findByIdAndEmployerId(employee.getId(), employerId);
        if(gtd == null) throw new Exception("Manager GTD not found");
        return String.valueOf(gtd.getGTD());
    }

    public static List<LocalDate> getNextWeeksOnDay(int dayOfWeek, int numWeeks) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate nextDate = LocalDate.now().plusWeeks(1);
        while (nextDate.getDayOfWeek().getValue() != dayOfWeek) {
            nextDate = nextDate.plusDays(1);
        }
        for(int i = 0; i < numWeeks; i++) {
            dates.add(nextDate);
            nextDate = nextDate.plusWeeks(1);
        }
        return dates;
    }
    public void setGtd(String employerId, String employeeEmail, int newGtd) throws Exception {
        Employer employer = employerRepository.findById(employerId);
        if(employer == null) throw new Exception("Employer does not exist");
        Employee employee = employeeRepository.findByEmployerIdAndEmail(employerId, employeeEmail);
        if(employee == null) throw new Exception("Employee not found");
        long employeeId = employee.getId();
        ManagerRequirements gtd = gtdRepository.findByIdAndEmployerId(employeeId, employerId);
        if(gtd != null) throw new Exception("GTD already exists");

        ManagerRequirements newRecord = new ManagerRequirements(employerId, employeeId, newGtd);
        gtdRepository.save(newRecord);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        boolean isPossible = true;
        List<LocalDate> nextTenWeeks = getNextWeeksOnDay(newGtd, 10);
        List<DirectReport> directReports = directReportRepository.findByEmployerIdAndManagerId(employerId, employeeId);
        if(directReports == null) throw new Exception("No direct reports found");
        for(LocalDate date: nextTenWeeks) {
            List<SeatReservations> reservationsOnDay = seatReservationsRepository.findByEmployerIdAndReservationDate(employerId, date.format(formatter));
            if(reservationsOnDay.size()+directReports.size() >= employer.getSeats()) {
                isPossible = false;
                break;
            }
        }
        if(!isPossible) throw new Exception("Capacity for GTD reservations not available");
        for(LocalDate date: nextTenWeeks) {
            seatReservationsService.createSeatReservation(employerId, employeeId, date, true);
            for(DirectReport report: directReports) {
                seatReservationsService.createSeatReservation(employerId, report.getReportId(), date, true);
            }
        }
    }

    public void deleteGtd(String employerId, String employeeEmail) throws Exception {
        Employer employer = employerRepository.findById(employerId);
        if(employer == null) throw new Exception("Employer does not exist");
        Employee employee = employeeRepository.findByEmployerIdAndEmail(employerId, employeeEmail);
        if(employee == null) throw new Exception("Employee not found");
        long employeeId = employee.getId();
        ManagerRequirements gtd = gtdRepository.findByIdAndEmployerId(employeeId, employerId);
        if(gtd == null) throw new Exception("GTD not found");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        List<LocalDate> nextTenWeeks = getNextWeeksOnDay(gtd.getGTD(), 10);
        List<DirectReport> directReports = directReportRepository.findByEmployerIdAndManagerId(employerId, employeeId);
        if(directReports == null) throw new Exception("No direct reports found");
        for(LocalDate date: nextTenWeeks) {
            SeatReservations managerReservation = seatReservationsRepository.findByEmployeeIdAndEmployerIdAndReservationDate(employeeId, employerId, date.format(formatter));
            if(managerReservation == null || !managerReservation.isGTD()) throw new Exception("No GTD reservation found");
            seatReservationsRepository.delete(managerReservation);

            for(DirectReport report: directReports) {
                SeatReservations reservation = seatReservationsRepository.findByEmployeeIdAndEmployerIdAndReservationDate(report.getReportId(), employerId, date.format(formatter));
                if(reservation == null || !reservation.isGTD()) throw new Exception("No GTD reservation found");
                seatReservationsRepository.delete(reservation);
            }
        }
        gtdRepository.delete(gtd);
    }
}
