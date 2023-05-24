package sjsu.cmpe275.entity;

public interface User<ID> {
    public String getName();

    public String getEmail();

    public String getToken();

    public int getMop();

    public void setMop(int newMOP);

    public User getEmployer();

    public ID getId();
}
