package sjsu.cmpe275.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class BulkEmployee {
    @Id
    private String employeeEmailId;
    private String employeeName;
    private String password;
    private String managerEmailId;
}
