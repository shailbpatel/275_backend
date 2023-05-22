package sjsu.cmpe275.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;



@Entity
@Table(name = "employee")
@IdClass(EmployeeId.class)
public class Employee {

    @Id
    @Column(name = "id")
    private long id;

    @Id
    @Column(name = "employer_id", nullable = false)
    private String employerId;

    @NotEmpty(message = "Name may not be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "title")
    private String title;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employer_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Employer employer;

    private Long manager_id;
    private String manager_employer_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns ({
            @JoinColumn(name = "manager_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "manager_employer_id", referencedColumnName = "employer_id", insertable = false, updatable = false),
    })
    @Access(AccessType.PROPERTY)
    private Employee Manager;

    public Employee() {
    }

    public Employee(long id, String employerId, String name, String email, String password, String title, Address address, Employer employer, Employee manager) {
        this.id = id;
        this.employerId = employerId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.title = title;
        this.address = address;
        this.employer = employer;
        this.setManager(manager);
    }

    public void setManager(Employee manager) {
        if (this.Manager != null) {
            if (this.Manager.equals(manager)) {
                return;
            }
        }

        this.Manager = manager;

        if (manager != null) {
            this.manager_id = manager.getId();
            this.manager_employer_id = manager.getEmployerId();
        } else {
            this.manager_id = null;
            this.manager_employer_id = null;
        }
    }

    public String getManagerEmployerId() {
        return manager_employer_id;
    }

    public void setManagerEmployerId(String managerEmployerId) {
        this.manager_employer_id = managerEmployerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmployerId() {
        return employerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

//    @XmlElement(name = "manager")
    public Employee getManager() {
        return Manager;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getManager_id() {
        return manager_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}