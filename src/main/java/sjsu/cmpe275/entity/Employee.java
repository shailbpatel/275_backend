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
@XmlRootElement(name = "employee")
@IdClass(EmployeeId.class)
@JsonInclude(Include.NON_NULL)
@JsonSerialize(using = FullEmployeeSerializer.class)
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

    @Column(name = "email")
    private String email;

    @Column(name = "title")
    private String title;

    @Embedded
    private Address address;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employer_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Employer employer;

    private Long manager_id;
    private String manager_employer_id;

    @JsonSerialize(using = ShallowEmployeeSerializer.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns ({
            @JoinColumn(name = "manager_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "manager_employer_id", referencedColumnName = "employer_id", insertable = false, updatable = false),
    })
    @Access(AccessType.PROPERTY)
    private Employee Manager;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "collaborator",
            joinColumns = {
                    @JoinColumn(name = "employee_id", referencedColumnName = "id"),
                    @JoinColumn(name = "employee_employer_id", referencedColumnName = "employer_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "collaborator_id", referencedColumnName = "id"),
                    @JoinColumn(name = "collaborator_employer_id", referencedColumnName = "employer_id")
            }
    )
    @Access(AccessType.PROPERTY)
    private List<Employee> collaborators;

    @OneToMany(mappedBy = "Manager", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Employee> reports;

    public Employee() {
    }

    public Employee(long id, String employerId, String name, String email, String title, Address address, Employer employer, Employee manager, List<Employee> collaborators, List<Employee> reports) {
        this.id = id;
        this.employerId = employerId;
        this.name = name;
        this.email = email;
        this.title = title;
        this.address = address;
        this.employer = employer;
        this.setManager(manager);
        this.collaborators = collaborators;
        this.reports = reports;
    }

    public Long getManagerId() {
        return manager_id;
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

    public List<Employee> getCollaborators() {
        return collaborators;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCollaborators(List<Employee> collaborators) {
        this.collaborators = collaborators;
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

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public List<Employee> getReports() {
        return reports;
    }

    public void setReports(List<Employee> reports) {
        this.reports = reports;
    }
}