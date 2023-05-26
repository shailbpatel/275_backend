package sjsu.cmpe275.entity;


import javax.persistence.*;

@Entity
@Table(name = "managerRequirements")
@IdClass(EmployeeId.class)
public class ManagerRequirements {

    @Id
    @Column(name = "id")
    private long id;

    @Id
    @Column(name = "employer_id")
    private String employerId;

    @Column(name= "GTD") // [1-5] : 1-> Mon, 2-> Tue
    private int GTD;

    public ManagerRequirements() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public int getGTD() {
        return GTD;
    }

    public void setGTD(int GTD) {
        this.GTD = GTD;
    }

    public ManagerRequirements(String employerId, long id, int GTD) {
        this.id = id;
        this.employerId = employerId;
        this.GTD = GTD;
    }
}
