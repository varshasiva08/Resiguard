package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByResidentId(Long residentId);
    List<ServiceRequest> findByServiceProviderId(Long providerId);
}
