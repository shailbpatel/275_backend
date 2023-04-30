package sjsu.cmpe275.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "employer")
@XmlRootElement(name = "employer")
@JsonSerialize(using = FullEmployerSerializer.class)
public class Employer {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank( message = "Name may not be empty or full of white spaces")
    private String name;

    @Column(name = "description")
    private String description;

    @Embedded
    private Address address;

    @Transient
    private List<Employee> employees;

    public Employer(String id, String name, String description, Address address, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.employees = employees;
    }

    public Employer() {
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "employees")
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @XmlElement(name = "address")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}