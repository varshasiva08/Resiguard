package com.resiguard.resiguard.service.impl;

import com.resiguard.resiguard.dto.request.DecisionRequest;
import com.resiguard.resiguard.dto.request.ServiceRequestDto;
import com.resiguard.resiguard.dto.request.WorkRequestDto;
import com.resiguard.resiguard.exception.BadRequestException;
import com.resiguard.resiguard.exception.ResourceNotFoundException;
import com.resiguard.resiguard.model.*;
import com.resiguard.resiguard.model.WorkRequest.AmountStatus;
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
    private final GuestEntryRepository guestEntryRepo;
    private final NotificationService notifService;

    public ServiceManagementServiceImpl(MaidRepository maidRepo, ServiceProviderRepository spRepo,
                                         ResidentRepository residentRepo, WorkRequestRepository workRequestRepo,
                                         ServiceRequestRepository serviceRequestRepo,
                                         GuestEntryRepository guestEntryRepo,
                                         NotificationService notifService) {
        this.maidRepo = maidRepo; this.spRepo = spRepo; this.residentRepo = residentRepo;
        this.workRequestRepo = workRequestRepo; this.serviceRequestRepo = serviceRequestRepo;
        this.guestEntryRepo = guestEntryRepo; this.notifService = notifService;
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
        String notifMsg = decision.accepted
            ? wr.getMaid().getName() + " ACCEPTED your work request. They can now propose their monthly amount."
            : wr.getMaid().getName() + " DECLINED your work request. Reason: " + (decision.reason != null && !decision.reason.isBlank() ? decision.reason : "No reason provided");
        notifService.sendToUser(wr.getResident().getId(), "Work Request Update", notifMsg, NotificationType.WORK_REQUEST);
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
                resident.getName() + " (Flat " + resident.getFlatNumber() + ") needs: " + dto.description, NotificationType.SERVICE_REQUEST);
        return saved;
    }
    @Override public ServiceRequest respondServiceRequest(Long requestId, Long providerId, DecisionRequest decision) {
        ServiceRequest sr = serviceRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        if (!sr.getServiceProvider().getId().equals(providerId)) throw new BadRequestException("Not authorized");
        if (sr.getStatus() != ServiceRequestStatus.PENDING) throw new BadRequestException("Already responded");
        sr.setStatus(decision.accepted ? ServiceRequestStatus.ACCEPTED : ServiceRequestStatus.REJECTED);
        sr.setUpdatedAt(LocalDateTime.now()); sr.setRejectionReason(decision.reason);
        ServiceRequest saved = serviceRequestRepo.save(sr);
        String notifMsg = decision.accepted
            ? sr.getServiceProvider().getName() + " ACCEPTED your service request."
            : sr.getServiceProvider().getName() + " DECLINED your service request. Reason: " + (decision.reason != null && !decision.reason.isBlank() ? decision.reason : "No reason provided");
        notifService.sendToUser(sr.getResident().getId(), "Service Request Update", notifMsg, NotificationType.SERVICE_REQUEST);
        return saved;
    }
    @Override public List<ServiceRequest> getProviderHistory(Long providerId) { return serviceRequestRepo.findByServiceProviderId(providerId); }
    @Override public List<WorkRequest> getResidentWorkRequests(Long residentId) { return workRequestRepo.findByResidentId(residentId); }
    @Override public List<ServiceRequest> getResidentServiceRequests(Long residentId) { return serviceRequestRepo.findByResidentId(residentId); }
    @Override public List<WorkRequest> getApprovedMaids() { return workRequestRepo.findByStatus(WorkRequestStatus.ACCEPTED); }

    @Override public WorkRequest proposeMaidAmount(Long requestId, Long maidId, String amount) {
        WorkRequest wr = workRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Work request not found"));
        if (!wr.getMaid().getId().equals(maidId)) throw new BadRequestException("Not authorized");
        if (wr.getStatus() != WorkRequestStatus.ACCEPTED) throw new BadRequestException("Work not accepted yet");
        wr.setProposedAmount(amount);
        wr.setAmountStatus(AmountStatus.PROPOSED);
        wr.setUpdatedAt(LocalDateTime.now());
        WorkRequest saved = workRequestRepo.save(wr);
        notifService.sendToUser(wr.getResident().getId(), "Maid Proposed Amount",
            wr.getMaid().getName() + " has proposed " + amount + " for the work. Please review and approve or reject.", NotificationType.WORK_REQUEST);
        return saved;
    }

    @Override public WorkRequest respondToMaidAmount(Long requestId, Long residentId, boolean approved) {
        WorkRequest wr = workRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Work request not found"));
        if (!wr.getResident().getId().equals(residentId)) throw new BadRequestException("Not authorized");
        wr.setAmountStatus(approved ? AmountStatus.APPROVED : AmountStatus.REJECTED);
        wr.setUpdatedAt(LocalDateTime.now());
        WorkRequest saved = workRequestRepo.save(wr);
        String msg = approved
            ? "Resident approved your proposed amount of " + wr.getProposedAmount() + "."
            : "Resident rejected your proposed amount of " + wr.getProposedAmount() + ". You may propose a new amount.";
        notifService.sendToUser(wr.getMaid().getId(), "Amount " + (approved ? "Approved" : "Rejected"), msg, NotificationType.WORK_REQUEST);
        if (!approved) { wr.setProposedAmount(null); wr.setAmountStatus(AmountStatus.NONE); workRequestRepo.save(wr); }
        return saved;
    }

    @Override public WorkRequest terminateWorkRequest(Long requestId, Long residentId) {
        WorkRequest wr = workRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Work request not found"));
        if (!wr.getResident().getId().equals(residentId)) throw new BadRequestException("Not authorized");
        wr.setStatus(WorkRequestStatus.COMPLETED);
        wr.setUpdatedAt(LocalDateTime.now());
        WorkRequest saved = workRequestRepo.save(wr);
        notifService.sendToUser(wr.getMaid().getId(), "Work Ended",
            "Resident " + wr.getResident().getName() + " has ended the work arrangement.", NotificationType.WORK_REQUEST);
        return saved;
    }

    @Override public ServiceRequest terminateServiceRequest(Long requestId, Long residentId) {
        ServiceRequest sr = serviceRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        if (!sr.getResident().getId().equals(residentId)) throw new BadRequestException("Not authorized");
        sr.setStatus(ServiceRequestStatus.COMPLETED);
        sr.setUpdatedAt(LocalDateTime.now());
        ServiceRequest saved = serviceRequestRepo.save(sr);
        notifService.sendToUser(sr.getServiceProvider().getId(), "Service Booking Ended",
            "Resident " + sr.getResident().getName() + " has ended the service arrangement.", NotificationType.SERVICE_REQUEST);
        return saved;
    }

    @Override public ServiceRequest markServiceCompleted(Long requestId, Long providerId) {
        ServiceRequest sr = serviceRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        if (!sr.getServiceProvider().getId().equals(providerId)) throw new BadRequestException("Not authorized");
        if (sr.getStatus() != ServiceRequestStatus.ACCEPTED) throw new BadRequestException("Not in accepted state");
        sr.setStatus(ServiceRequestStatus.COMPLETED);
        sr.setCompletedAt(LocalDateTime.now());
        sr.setUpdatedAt(LocalDateTime.now());
        ServiceRequest saved = serviceRequestRepo.save(sr);
        notifService.sendToUser(sr.getResident().getId(), "Service Completed",
            sr.getServiceProvider().getName() + " has marked your service request as completed. Please rate their service.", NotificationType.SERVICE_REQUEST);
        return saved;
    }

    @Override public WorkRequest markWorkCompleted(Long requestId, Long maidId) {
        WorkRequest wr = workRequestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Work request not found"));
        if (!wr.getMaid().getId().equals(maidId)) throw new BadRequestException("Not authorized");
        if (wr.getStatus() != WorkRequestStatus.ACCEPTED) throw new BadRequestException("Not in accepted state");
        wr.setStatus(WorkRequestStatus.COMPLETED);
        wr.setUpdatedAt(LocalDateTime.now());
        WorkRequest saved = workRequestRepo.save(wr);
        notifService.sendToUser(wr.getResident().getId(), "Work Completed",
            wr.getMaid().getName() + " has marked the work as completed. Please rate their service.", NotificationType.WORK_REQUEST);
        return saved;
    }

    @Override public void cancelGuestEntry(Long entryId, Long residentId) {
        GuestEntry entry = guestEntryRepo.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Entry not found"));
        if (!entry.getResident().getId().equals(residentId)) throw new BadRequestException("Not authorized");
        entry.setStatus(com.resiguard.resiguard.model.enums.GuestStatus.REJECTED);
        entry.setPassCodeExpired(true);
        entry.setRejectionReason("Removed by resident");
        guestEntryRepo.save(entry);
        notifService.sendToUser(entry.getGuest().getId(), "Entry Cancelled",
            "Your entry pass for " + entry.getResident().getName() + "'s flat has been cancelled by the resident.", NotificationType.GUEST_REQUEST);
    }
}
