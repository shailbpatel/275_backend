package sjsu.cmpe275.entity;

import javax.persistence.*;

@Entity
@Table(name= "directReport")
@IdClass(ManagerReportID.class)
public class DirectReport {

    @Id
    @Column(name = "employerId")
    private String employerId;

    @Id
    @Column(name = "managerId")
    private long managerId;

    @Id
    @Column(name = "reportId")
    private long reportId;

    public DirectReport() {}

    public DirectReport(String employerId, long managerId, long reportId) {
        this.employerId = employerId;
        this.managerId = managerId;
        this.reportId = reportId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }
}
