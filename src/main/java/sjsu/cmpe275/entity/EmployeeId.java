package sjsu.cmpe275.entity;

import java.io.Serializable;
import java.util.Objects;

public class EmployeeId implements Serializable {
    private long id;
    private String employerId;

    public EmployeeId() {
    }

    public EmployeeId(long id, String employerId) {
        this.id = id;
        this.employerId = employerId;
    }

    public long getId() {
        return id;
    }

    public String getEmployerId() {
        return employerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeId that = (EmployeeId) o;
        return id == that.id &&
                Objects.equals(employerId, that.employerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employerId);
    }
}