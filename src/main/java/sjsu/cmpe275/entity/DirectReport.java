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
    private Long managerId;

    @Id
    @Column(name = "reportId")
    private Long reportId;

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}
