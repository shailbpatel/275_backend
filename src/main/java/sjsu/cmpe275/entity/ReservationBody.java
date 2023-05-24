package sjsu.cmpe275.entity;

public class ReservationBody {

    String employeeId;
    String employerId;
    String startDate;
    String endDate;
    boolean isGTD;
    boolean isPreemptable;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
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

    public boolean isGTD() {
        return isGTD;
    }

    public void setGTD(boolean GTD) {
        isGTD = GTD;
    }

    public boolean isPreemptable() {
        return isPreemptable;
    }

    public void setPreemptable(boolean preemptable) {
        isPreemptable = preemptable;
    }
}

