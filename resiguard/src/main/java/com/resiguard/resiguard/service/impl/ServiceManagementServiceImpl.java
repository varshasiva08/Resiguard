package com.resiguard.resiguard.service.impl;

import com.resiguard.resiguard.dto.request.DecisionRequest;
import com.resiguard.resiguard.dto.request.ServiceRequestDto;
import com.resiguard.resiguard.dto.request.WorkRequestDto;
import com.resiguard.resiguard.exception.BadRequestException;
import com.resiguard.resiguard.exception.ResourceNotFoundException;
import com.resiguard.resiguard.model.*;
import com.resiguard.resiguard.model.enums.*;
import com.resiguard.resiguard.repository.*;
import com.resiguard.resiguard.service.NotificationService;
import com.resiguard.resiguard.service.ServiceManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @Transactional
public class ServiceManagementServiceImpl implements ServiceManagementService {
    private final MaidRepository maidRepo;
    private final ServiceProviderRepository spRepo;
    private final ResidentRepository residentRepo;
    private final WorkRequestRepository workRequestRepo;
    private final ServiceRequestRepository serviceRequestRepo;
    private final NotificationService notifService;

    public ServiceManagementServiceImpl(MaidRepository maidRepo, ServiceProviderRepository spRepo,
                                         ResidentRepository residentRepo, WorkRequestRepository workRequestRepo,
                                         ServiceRequestRepository serviceRequestRepo, NotificationService notifService) {
        this.maidRepo = maidRepo; this.spRepo = spRepo; this.residentRepo = residentRepo;
        this.workRequestRepo = workRequestRepo; this.serviceRequestRepo = serviceRequestRepo;
        this.notifService = notifService;
    }

    @Override public List<Maid> searchMaids(String name) {
        return (name != null && !name.isBlank()) ? maidRepo.findByNameContainingIgnoreCase(name)
                : maidRepo.findByStatus(ProfileStatus.ACTIVE);
    }
    @Override public WorkRequest hireMaid(Long residentId, WorkRequestDto dto) {
        Resident resident = residentRepo.findById(residentId).orElseThrow(() -> new ResourceNotFoundException("Resident not found"));
        Maid maid = maidRepo.findById(dto.maidId).orElseThrow(() -> new ResourceNotFoundException("Maid not found"));
        if (maid.getStatus() != ProfileStatus.ACTIVE) throw new BadRequestException("Maid not active");
        WorkRequest wr = new WorkRequest();
        wr.setResident(resident); wr.setMaid(maid); wr.setDescription(dto.description);
        wr.setStartDate(dto.startDate); wr.setWorkTimings(dto.workTimings);
        WorkRequest saved = workRequestRepo.save(wr);
        notifService.sendToUser(maid.getId(), "New Work Request",
                resident.getName() + " (Flat " + resident.getFlatNumber() + ") wants to hire you.", NotificationType.WORK_REQUEST);
        return saved;
    }
    @Override public WorkRequest respondWorkRequest(Long requestId, Long maidId, DecisionRequest decision) {
        WorkRequest wr = workRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Work request not found"));
        if (!wr.getMaid().getId().equals(maidId)) throw new BadRequestException("Not authorized");
        if (wr.getStatus() != WorkRequestStatus.PENDING) throw new BadRequestException("Already responded");
        wr.setStatus(decision.accepted ? WorkRequestStatus.ACCEPTED : WorkRequestStatus.REJECTED);
        wr.setUpdatedAt(LocalDateTime.now()); wr.setRejectionReason(decision.reason);
        WorkRequest saved = workRequestRepo.save(wr);
        notifService.sendToUser(wr.getResident().getId(), "Work Request Update",
                wr.getMaid().getName() + " " + wr.getStatus().name() + " your request.", NotificationType.WORK_REQUEST);
        return saved;
    }
    @Override public List<WorkRequest> getMaidWorkHistory(Long maidId) { return workRequestRepo.findByMaidId(maidId); }
    @Override public List<ServiceProvider> searchProviders(String name, ServiceCategory category) {
        if (category != null) return spRepo.findByCategoryAndStatus(category, ProfileStatus.ACTIVE);
        return (name != null && !name.isBlank()) ? spRepo.findByNameContainingIgnoreCase(name)
                : spRepo.findByStatus(ProfileStatus.ACTIVE);
    }
    @Override public ServiceRequest requestService(Long residentId, ServiceRequestDto dto) {
        Resident resident = residentRepo.findById(residentId).orElseThrow(() -> new ResourceNotFoundException("Resident not found"));
        ServiceProvider sp = spRepo.findById(dto.serviceProviderId).orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        if (sp.getStatus() != ProfileStatus.ACTIVE) throw new BadRequestException("Provider not active");
        ServiceRequest sr = new ServiceRequest();
        sr.setResident(resident); sr.setServiceProvider(sp); sr.setDescription(dto.description);
        sr.setPreferredDateTime(dto.preferredDateTime);
        ServiceRequest saved = serviceRequestRepo.save(sr);
        notifService.sendToUser(sp.getId(), "New Service Request",
                resident.getName() + " needs: " + dto.description, NotificationType.SERVICE_REQUEST);
        return saved;
    }
    @Override public ServiceRequest respondServiceRequest(Long requestId, Long providerId, DecisionRequest decision) {
        ServiceRequest sr = serviceRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        if (!sr.getServiceProvider().getId().equals(providerId)) throw new BadRequestException("Not authorized");
        if (sr.getStatus() != ServiceRequestStatus.PENDING) throw new BadRequestException("Already responded");
        sr.setStatus(decision.accepted ? ServiceRequestStatus.ACCEPTED : ServiceRequestStatus.REJECTED);
        sr.setUpdatedAt(LocalDateTime.now()); sr.setRejectionReason(decision.reason);
        ServiceRequest saved = serviceRequestRepo.save(sr);
        notifService.sendToUser(sr.getResident().getId(), "Service Request Update",
                sr.getServiceProvider().getName() + " " + sr.getStatus().name() + " your request.", NotificationType.SERVICE_REQUEST);
        return saved;
    }
    @Override public List<ServiceRequest> getProviderHistory(Long providerId) { return serviceRequestRepo.findByServiceProviderId(providerId); }
    @Override public List<WorkRequest> getResidentWorkRequests(Long residentId) { return workRequestRepo.findByResidentId(residentId); }
    @Override public List<ServiceRequest> getResidentServiceRequests(Long residentId) { return serviceRequestRepo.findByResidentId(residentId); }
}
