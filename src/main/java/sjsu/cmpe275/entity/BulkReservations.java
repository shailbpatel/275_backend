package sjsu.cmpe275.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class BulkReservations {

    @Id
    private String employeeEmailId;
    private String startDate;
    private String endDate;
}
