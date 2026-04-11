package com.resiguard.resiguard.service;
import com.resiguard.resiguard.dto.request.GuestEntryDecisionRequest;
import com.resiguard.resiguard.dto.request.GuestEntryRequest;
import com.resiguard.resiguard.model.EntryLog;
import com.resiguard.resiguard.model.GuestEntry;
import com.resiguard.resiguard.model.enums.EntryLogType;
import java.util.List;
public interface VisitorService {
    GuestEntry requestEntry(Long guestId, GuestEntryRequest request);
    GuestEntry decideEntry(Long entryId, Long residentId, GuestEntryDecisionRequest decision);
    GuestEntry verifyPassCode(String passCode);
    EntryLog logEntry(Long entryId, Long guardId, EntryLogType type, String notes);
    List<GuestEntry> getResidentHistory(Long residentId);
    List<GuestEntry> getGuestHistory(Long guestId);
}
