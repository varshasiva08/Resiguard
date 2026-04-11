package com.resiguard.resiguard.service.impl;

import com.resiguard.resiguard.dto.request.GuestEntryDecisionRequest;
import com.resiguard.resiguard.dto.request.GuestEntryRequest;
import com.resiguard.resiguard.exception.BadRequestException;
import com.resiguard.resiguard.exception.ResourceNotFoundException;
import com.resiguard.resiguard.model.*;
import com.resiguard.resiguard.model.enums.*;
import com.resiguard.resiguard.repository.*;
import com.resiguard.resiguard.service.NotificationService;
import com.resiguard.resiguard.service.VisitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service @Transactional
public class VisitorServiceImpl implements VisitorService {
    private final GuestEntryRepository entryRepo;
    private final GuestRepository guestRepo;
    private final ResidentRepository residentRepo;
    private final SecurityGuardRepository guardRepo;
    private final EntryLogRepository entryLogRepo;
    private final NotificationService notifService;

    public VisitorServiceImpl(GuestEntryRepository entryRepo, GuestRepository guestRepo,
                               ResidentRepository residentRepo, SecurityGuardRepository guardRepo,
                               EntryLogRepository entryLogRepo, NotificationService notifService) {
        this.entryRepo = entryRepo; this.guestRepo = guestRepo; this.residentRepo = residentRepo;
        this.guardRepo = guardRepo; this.entryLogRepo = entryLogRepo; this.notifService = notifService;
    }

    @Override
    public GuestEntry requestEntry(Long guestId, GuestEntryRequest req) {
        Guest guest = guestRepo.findById(guestId).orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
        Resident resident = residentRepo.findById(req.residentId).orElseThrow(() -> new ResourceNotFoundException("Resident not found"));
        GuestEntry entry = new GuestEntry();
        entry.setGuest(guest); entry.setResident(resident); entry.setPurpose(req.purpose);
        GuestEntry saved = entryRepo.save(entry);
        notifService.sendToUser(resident.getId(), "Guest Entry Request",
                guest.getName() + " wants to visit. Purpose: " + req.purpose, NotificationType.GUEST_REQUEST);
        return saved;
    }

    @Override
    public GuestEntry decideEntry(Long entryId, Long residentId, GuestEntryDecisionRequest decision) {
        GuestEntry entry = entryRepo.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Entry not found"));
        if (!entry.getResident().getId().equals(residentId)) throw new BadRequestException("Not authorized");
        if (entry.getStatus() != GuestStatus.PENDING) throw new BadRequestException("Already decided");
        entry.setStatus(decision.approved ? GuestStatus.APPROVED : GuestStatus.REJECTED);
        entry.setDecidedAt(LocalDateTime.now());
        if (decision.approved) entry.setEntryPassCode(UUID.randomUUID().toString().substring(0,8).toUpperCase());
        else entry.setRejectionReason(decision.rejectionReason);
        GuestEntry saved = entryRepo.save(entry);
        String msg = decision.approved ? "Entry APPROVED. Pass: " + entry.getEntryPassCode()
                : "Entry REJECTED. Reason: " + decision.rejectionReason;
        notifService.sendToUser(entry.getGuest().getId(), "Entry Decision", msg, NotificationType.GUEST_REQUEST);
        return saved;
    }

    @Override
    public GuestEntry verifyPassCode(String passCode) {
        return entryRepo.findByEntryPassCode(passCode).orElseThrow(() -> new ResourceNotFoundException("Invalid pass code"));
    }

    @Override
    public EntryLog logEntry(Long entryId, Long guardId, EntryLogType type, String notes) {
        GuestEntry entry = entryRepo.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Entry not found"));
        SecurityGuard guard = guardRepo.findById(guardId).orElseThrow(() -> new ResourceNotFoundException("Guard not found"));
        EntryLog log = new EntryLog();
        log.setGuestEntry(entry); log.setGuard(guard); log.setLogType(type); log.setNotes(notes);
        EntryLog saved = entryLogRepo.save(log);
        // Notify resident about entry/exit
        String action = (type == EntryLogType.ENTRY) ? "entered" : "exited";
        String guestName = entry.getGuest() != null ? entry.getGuest().getName() : "Your guest";
        notifService.sendToUser(entry.getResident().getId(),
                "Gate " + (type == EntryLogType.ENTRY ? "Entry" : "Exit") + " Alert",
                guestName + " has " + action + " the premises. Logged by Guard " + guard.getName() + ".",
                NotificationType.GENERAL);
        return saved;
    }

    @Override public List<GuestEntry> getResidentHistory(Long residentId) { return entryRepo.findByResidentId(residentId); }
    @Override public List<GuestEntry> getGuestHistory(Long guestId) { return entryRepo.findByGuestId(guestId); }
}
