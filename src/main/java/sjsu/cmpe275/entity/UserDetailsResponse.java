package sjsu.cmpe275.entity;


public class UserDetailsResponse {
    private String name;
    private String email;
    private String role;
    private boolean isVerified;
    private boolean isGoogle;
    private String employerId;
    private boolean isManager;
    private long employeeId;

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public UserDetailsResponse() {
    }

    public boolean getis_manager() {
        return isManager;
    }

    public void setIsManager(boolean manager) {
        isManager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public boolean getis_google() {
        return isGoogle;
    }

    public void setIsGoogle(boolean isGoogle) {
        this.isGoogle = isGoogle;
    }

    public boolean getis_verified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}
