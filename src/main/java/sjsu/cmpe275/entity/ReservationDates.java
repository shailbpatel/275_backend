package sjsu.cmpe275.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReservationDates {
    String startDate;

    String endDate;
    @Id
    private Long id;


    public ReservationDates() {

    }

    public ReservationDates(String startDate, String endDate, Long id) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
