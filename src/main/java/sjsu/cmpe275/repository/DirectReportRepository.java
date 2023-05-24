package sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;
import sjsu.cmpe275.entity.DirectReport;

import java.util.List;

public interface DirectReportRepository extends CrudRepository<DirectReport, Long>  {
    DirectReport findByEmployerIdAndReportId(String employerId, long reportId);
    List<DirectReport> findByEmployerIdAndManagerId(String employerId, long managerId);
}
