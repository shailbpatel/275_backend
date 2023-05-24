package sjsu.cmpe275.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.UUID;
import sjsu.cmpe275.entity.Address;

@Entity
@Table(name = "employer")
public class Employer implements User<String> {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank( message = "Name may not be empty or full of white spaces")
    private String name;

    @Embedded
    private Address address;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "seats", nullable = false)
    @Min(value = 3, message = "The minimum number of seats is 3")
    @Max(value = 100, message = "The maximum number of seats is 100")
    private int seats;

    @Column(name = "is_google")
    private boolean isGoogle;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "token")
    private String token;

    @Column(name = "mop")
    private int mop;

    public Employer(String id, String name, Address address, String email, String password, int seats, boolean is_google, boolean is_verified) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.seats = seats;
        this.isGoogle = is_google;
        this.isVerified = is_verified;
        this.token = UUID.randomUUID().toString();
    }

    public Employer(String id, String name, Address address, String email, String password, int seats, boolean is_google, boolean is_verified, int mop) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.seats = seats;
        this.isGoogle = is_google;
        this.isVerified = is_verified;
        this.token = UUID.randomUUID().toString();
        this.mop = mop;
    }

    public Employer() {

    }

    @Override
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

    public boolean isIsGoogle() {
        return isGoogle;
    }

    public void setIsGoogle(boolean is_google) {
        this.isGoogle = is_google;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean is_verified) {
        this.isVerified = is_verified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int getMop() {
        return mop;
    }

    @Override
    public void setMop(int mop) {
        this.mop = mop;
    }

    @Override
    @JsonIgnore
    public Employer getEmployer() {return this;}
}