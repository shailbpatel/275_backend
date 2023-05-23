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


    //TODO: map days to integer for both the  fields
    @Column(name = "MOP") // [1-5] : 1-> Mon, 2-> Tue
    private int MOP;

    @Column(name= "GTD") // [1-5] : 1-> Mon, 2-> Tue
    private int GTD;

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

    public int getMOP() {
        return MOP;
    }

    public void setMOP(int MOP) {
        this.MOP = MOP;
    }

    public int getGTD() {
        return GTD;
    }

    public void setGTD(int GTD) {
        this.GTD = GTD;
    }
}
