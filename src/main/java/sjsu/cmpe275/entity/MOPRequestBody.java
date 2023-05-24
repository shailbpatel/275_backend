package sjsu.cmpe275.entity;

public class MOPRequestBody {
    private String email;
    private String role;
    private String employerId;
    private int mop;
    public int getMop() {
        return mop;
    }
    public String getEmail() {
        return email;
    }
    public String getRole() {
        return role;
    }
    public String getEmployerId() {
        return employerId;
    }

    public MOPRequestBody() {
    }
}
