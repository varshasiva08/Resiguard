package com.resiguard.resiguard.service;
import com.resiguard.resiguard.dto.request.DecisionRequest;
import com.resiguard.resiguard.dto.request.ServiceRequestDto;
import com.resiguard.resiguard.dto.request.WorkRequestDto;
import com.resiguard.resiguard.model.*;
import com.resiguard.resiguard.model.enums.ServiceCategory;
import java.util.List;
public interface ServiceManagementService {
    List<Maid> searchMaids(String name);
    WorkRequest hireMaid(Long residentId, WorkRequestDto dto);
    WorkRequest respondWorkRequest(Long requestId, Long maidId, DecisionRequest decision);
    List<WorkRequest> getMaidWorkHistory(Long maidId);
    List<ServiceProvider> searchProviders(String name, ServiceCategory category);
    ServiceRequest requestService(Long residentId, ServiceRequestDto dto);
    ServiceRequest respondServiceRequest(Long requestId, Long providerId, DecisionRequest decision);
    List<ServiceRequest> getProviderHistory(Long providerId);
    List<WorkRequest> getResidentWorkRequests(Long residentId);
    List<ServiceRequest> getResidentServiceRequests(Long residentId);
    List<WorkRequest> getApprovedMaids();
}
