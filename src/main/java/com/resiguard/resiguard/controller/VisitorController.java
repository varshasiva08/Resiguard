package com.resiguard.resiguard.controller;
import com.resiguard.resiguard.dto.request.GuestEntryDecisionRequest;
import com.resiguard.resiguard.dto.request.GuestEntryRequest;
import com.resiguard.resiguard.dto.response.ApiResponse;
import com.resiguard.resiguard.model.enums.EntryLogType;
import com.resiguard.resiguard.service.VisitorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/visitor")
public class VisitorController {
    private final VisitorService visitorService;
    public VisitorController(VisitorService visitorService) { this.visitorService = visitorService; }

    @PostMapping("/guest/{guestId}/request")
    public ResponseEntity<ApiResponse<?>> requestEntry(@PathVariable Long guestId, @Valid @RequestBody GuestEntryRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Entry requested", visitorService.requestEntry(guestId, req)));
    }
    @PutMapping("/entry/{entryId}/decide")
    public ResponseEntity<ApiResponse<?>> decideEntry(@PathVariable Long entryId, @RequestParam Long residentId,
            @Valid @RequestBody GuestEntryDecisionRequest decision) {
        return ResponseEntity.ok(ApiResponse.ok("Decision recorded", visitorService.decideEntry(entryId, residentId, decision)));
    }
    @GetMapping("/verify/{passCode}")
    public ResponseEntity<ApiResponse<?>> verify(@PathVariable String passCode) {
        return ResponseEntity.ok(ApiResponse.ok("Valid pass", visitorService.verifyPassCode(passCode)));
    }
    @PostMapping("/entry/{entryId}/log")
    public ResponseEntity<ApiResponse<?>> log(@PathVariable Long entryId, @RequestParam Long guardId,
            @RequestParam EntryLogType type, @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(ApiResponse.ok("Logged", visitorService.logEntry(entryId, guardId, type, notes)));
    }
    @GetMapping("/history/resident/{residentId}")
    public ResponseEntity<ApiResponse<?>> residentHistory(@PathVariable Long residentId) {
        return ResponseEntity.ok(ApiResponse.ok("History", visitorService.getResidentHistory(residentId)));
    }
    @GetMapping("/history/guest/{guestId}")
    public ResponseEntity<ApiResponse<?>> guestHistory(@PathVariable Long guestId) {
        return ResponseEntity.ok(ApiResponse.ok("History", visitorService.getGuestHistory(guestId)));
    }
    @GetMapping("/active-on-premises")
    public ResponseEntity<ApiResponse<?>> activeOnPremises() {
        return ResponseEntity.ok(ApiResponse.ok("Active guests", visitorService.getActiveGuestsOnPremises()));
    }
    @GetMapping("/guest/{guestId}/active-passes")
    public ResponseEntity<ApiResponse<?>> guestActivePasses(@PathVariable Long guestId) {
        return ResponseEntity.ok(ApiResponse.ok("Active passes", visitorService.getGuestActiveApproved(guestId)));
    }
    @PostMapping("/maid-entry/log")
    public ResponseEntity<ApiResponse<?>> logMaidEntry(@RequestParam Long guardId, @RequestParam Long maidId,
            @RequestParam Long workRequestId, @RequestParam String type) {
        visitorService.logMaidEntry(guardId, maidId, workRequestId, type);
        return ResponseEntity.ok(ApiResponse.ok("Maid " + type + " logged and resident notified", null));
    }
    @GetMapping("/entry-logs/resident/{residentId}")
    public ResponseEntity<ApiResponse<?>> residentEntryLogs(@PathVariable Long residentId) {
        return ResponseEntity.ok(ApiResponse.ok("Entry logs", visitorService.getResidentEntryLogs(residentId)));
    }
    @GetMapping("/active-entries")
    public ResponseEntity<ApiResponse<?>> activeEntries() {
        return ResponseEntity.ok(ApiResponse.ok("Active entries", visitorService.getActiveGuestsOnPremises()));
    }

    // FIX 1: Search residents by flat number (door number) so guests don't need to know resident ID
    @GetMapping("/residents/search")
    public ResponseEntity<ApiResponse<?>> searchResidentsByFlat(@RequestParam(required = false) String flatNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Residents", visitorService.searchResidentsByFlat(flatNumber)));
    }
}
