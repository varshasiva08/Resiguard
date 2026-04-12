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
    // New: maid proposes amount after accepting
    WorkRequest proposeMaidAmount(Long requestId, Long maidId, String amount);
    // New: resident responds to proposed amount
    WorkRequest respondToMaidAmount(Long requestId, Long residentId, boolean approved);
    // New: resident terminates maid/service relationship
    WorkRequest terminateWorkRequest(Long requestId, Long residentId);
    ServiceRequest terminateServiceRequest(Long requestId, Long residentId);
    // New: provider marks service as completed
    ServiceRequest markServiceCompleted(Long requestId, Long providerId);
    // New: maid marks work as completed
    WorkRequest markWorkCompleted(Long requestId, Long maidId);
    // New: resident removes/cancels guest entry
    void cancelGuestEntry(Long entryId, Long residentId);
}
