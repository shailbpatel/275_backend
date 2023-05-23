package sjsu.cmpe275.entity;


import javax.persistence.*;

@Entity
@Table(name = "seatReservations")
public class SeatReservations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="reservationId", nullable = false)
    private int reservationId;

    @Column(name = "employerId")
    private String employerId;



    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Column(name = "employeeId")
    private long employeeId;

    @Column(name = "startDate")
    private String startDate;

    @Column(name = "endDate")
    private String endDate;

    @Column(name = "isPreemptable")
    private boolean isPreemptable;

    @Column(name = "isGTD")
    private boolean isGTD;
    public SeatReservations(String employerId, Long employeeId, String startDate, String endDate, boolean isPreemptable, boolean isGTD) {
    }
    public SeatReservations(int reservationId, String employerId, long employeeId, String startDate, String endDate, boolean isPreemptable, boolean isGTD) {
        this.reservationId = reservationId;
        this.employerId = employerId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPreemptable = isPreemptable;
        this.isGTD = isGTD;
    }

    public SeatReservations() {

    }

    public boolean isPreemptable() {
        return isPreemptable;
    }

    public void setPreemptable(boolean preemptable) {
        isPreemptable = preemptable;
    }

    public boolean isGTD() {
        return isGTD;
    }

    public void setGTD(boolean GTD) {
        isGTD = GTD;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }


}
