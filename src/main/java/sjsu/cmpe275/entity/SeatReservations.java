package sjsu.cmpe275.entity;


import javax.persistence.*;

@Entity
@Table(name = "seatReservations")
public class SeatReservations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservationId", nullable = false)
    private int reservationId;

    @Column(name = "employerId")
    private String employerId;

    @Column(name = "employeeId")
    private long employeeId;

    @Column(name = "reservationDate")
    private String reservationDate;

    @Column(name = "isPreemptable")
    private boolean isPreemptable;

    @Column(name = "isGTD")
    private boolean isGTD;

    public SeatReservations(String employerId, long employeeId, String reservationDate, boolean isGTD, boolean isPreemptable) {
        this.employerId = employerId;
        this.employeeId = employeeId;
        this.reservationDate = reservationDate;
        this.isPreemptable = isPreemptable;
        this.isGTD = isGTD;
    }

    public SeatReservations() {
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

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
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
}
