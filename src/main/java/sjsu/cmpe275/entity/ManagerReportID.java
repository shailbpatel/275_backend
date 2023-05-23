package sjsu.cmpe275.entity;

import java.io.Serializable;
import java.util.Objects;

public class ManagerReportID implements Serializable {

    private String employerId;

    private Long managerId;

    private Long reportId;

    public ManagerReportID() {
    }

    public ManagerReportID(Long managerId, String employerId, Long reportId) {
        this.managerId = managerId;
        this.employerId = employerId;
        this.reportId = reportId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public Long getParentManagerId() {
        return reportId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerReportID that = (ManagerReportID) o;
        return Objects.equals(managerId, that.managerId) &&
                Objects.equals(employerId, that.employerId) &&
                Objects.equals(reportId, that.reportId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(managerId, employerId, reportId);
    }
}
