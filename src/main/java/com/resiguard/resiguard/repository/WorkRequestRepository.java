package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.WorkRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface WorkRequestRepository extends JpaRepository<WorkRequest, Long> {
    List<WorkRequest> findByResidentId(Long residentId);
    List<WorkRequest> findByMaidId(Long maidId);
    List<WorkRequest> findByStatus(com.resiguard.resiguard.model.enums.WorkRequestStatus status);
}
