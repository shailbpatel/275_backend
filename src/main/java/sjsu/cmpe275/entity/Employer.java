package sjsu.cmpe275.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
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

    @Embedded
    private Address address;

    @Transient
    private List<Employee> employees;

    @Column(name = "email")
    private String email;

    @Column(name = "seats")
    private int seats;

    @Column(name = "is_google")
    private boolean is_google;

    @Column(name = "is_verified")
    private boolean is_verified;

    public Employer(String id, String name, Address address, String email, int seats, List<Employee> employees, boolean is_google, boolean is_verified) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.employees = employees;
        this.email = email;
        this.seats = seats;
        this.is_google = is_google;
        this.is_verified = is_verified;
    }

    public Employer() {

    }

    public Employer(String id, String name, Address address, ArrayList<Employee> employees) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public boolean isIs_google() {
        return is_google;
    }

    public void setIs_google(boolean is_google) {
        this.is_google = is_google;
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }
}