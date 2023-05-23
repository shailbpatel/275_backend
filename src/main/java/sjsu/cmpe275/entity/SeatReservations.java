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

    @Column(name = "employeeId")
    private long employeeId;

    @Column(name = "isPreemptable")
    private boolean isPreemptable;

    @Column(name = "isGTD")
    private boolean isGTD;

    public SeatReservations(String employerId, long employeeId, boolean isPreemptable, boolean isGTD) {
        this.employerId = employerId;
        this.employeeId = employeeId;
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
