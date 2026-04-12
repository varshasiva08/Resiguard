package com.resiguard.resiguard.controller;
import com.resiguard.resiguard.dto.request.DecisionRequest;
import com.resiguard.resiguard.dto.request.ServiceRequestDto;
import com.resiguard.resiguard.dto.request.WorkRequestDto;
import com.resiguard.resiguard.dto.response.ApiResponse;
import com.resiguard.resiguard.model.enums.ServiceCategory;
import com.resiguard.resiguard.service.ServiceManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/service")
public class ServiceController {
    private final ServiceManagementService svc;
    public ServiceController(ServiceManagementService svc) { this.svc = svc; }

    @GetMapping("/maids")
    public ResponseEntity<ApiResponse<?>> searchMaids(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(ApiResponse.ok("Maids", svc.searchMaids(name)));
    }
    @PostMapping("/maid/hire/{residentId}")
    public ResponseEntity<ApiResponse<?>> hireMaid(@PathVariable Long residentId, @Valid @RequestBody WorkRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Work request sent", svc.hireMaid(residentId, dto)));
    }
    @PutMapping("/maid/request/{requestId}/respond")
    public ResponseEntity<ApiResponse<?>> respondWork(@PathVariable Long requestId, @RequestParam Long maidId,
            @Valid @RequestBody DecisionRequest decision) {
        return ResponseEntity.ok(ApiResponse.ok("Response recorded", svc.respondWorkRequest(requestId, maidId, decision)));
    }
    @GetMapping("/maid/{maidId}/history")
    public ResponseEntity<ApiResponse<?>> maidHistory(@PathVariable Long maidId) {
        return ResponseEntity.ok(ApiResponse.ok("History", svc.getMaidWorkHistory(maidId)));
    }
    // Maid proposes amount
    @PutMapping("/maid/request/{requestId}/propose-amount")
    public ResponseEntity<ApiResponse<?>> proposeMaidAmount(@PathVariable Long requestId,
            @RequestParam Long maidId, @RequestBody Map<String,String> body) {
        return ResponseEntity.ok(ApiResponse.ok("Amount proposed", svc.proposeMaidAmount(requestId, maidId, body.get("amount"))));
    }
    // Resident responds to proposed amount
    @PutMapping("/maid/request/{requestId}/respond-amount")
    public ResponseEntity<ApiResponse<?>> respondToAmount(@PathVariable Long requestId,
            @RequestParam Long residentId, @RequestParam boolean approved) {
        return ResponseEntity.ok(ApiResponse.ok("Response recorded", svc.respondToMaidAmount(requestId, residentId, approved)));
    }
    // Maid marks work as completed
    @PutMapping("/maid/request/{requestId}/complete")
    public ResponseEntity<ApiResponse<?>> completeMaidWork(@PathVariable Long requestId, @RequestParam Long maidId) {
        return ResponseEntity.ok(ApiResponse.ok("Marked complete", svc.markWorkCompleted(requestId, maidId)));
    }
    // Resident terminates maid
    @PutMapping("/maid/request/{requestId}/terminate")
    public ResponseEntity<ApiResponse<?>> terminateMaid(@PathVariable Long requestId, @RequestParam Long residentId) {
        return ResponseEntity.ok(ApiResponse.ok("Work ended", svc.terminateWorkRequest(requestId, residentId)));
    }

    @GetMapping("/providers")
    public ResponseEntity<ApiResponse<?>> searchProviders(@RequestParam(required = false) String name,
            @RequestParam(required = false) ServiceCategory category) {
        return ResponseEntity.ok(ApiResponse.ok("Providers", svc.searchProviders(name, category)));
    }
    @PostMapping("/provider/request/{residentId}")
    public ResponseEntity<ApiResponse<?>> requestService(@PathVariable Long residentId, @Valid @RequestBody ServiceRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Service request sent", svc.requestService(residentId, dto)));
    }
    @PutMapping("/provider/request/{requestId}/respond")
    public ResponseEntity<ApiResponse<?>> respondService(@PathVariable Long requestId, @RequestParam Long providerId,
            @Valid @RequestBody DecisionRequest decision) {
        return ResponseEntity.ok(ApiResponse.ok("Response recorded", svc.respondServiceRequest(requestId, providerId, decision)));
    }
    // Provider marks service as done
    @PutMapping("/provider/request/{requestId}/complete")
    public ResponseEntity<ApiResponse<?>> completeService(@PathVariable Long requestId, @RequestParam Long providerId) {
        return ResponseEntity.ok(ApiResponse.ok("Marked complete", svc.markServiceCompleted(requestId, providerId)));
    }
    // Resident terminates service provider
    @PutMapping("/provider/request/{requestId}/terminate")
    public ResponseEntity<ApiResponse<?>> terminateService(@PathVariable Long requestId, @RequestParam Long residentId) {
        return ResponseEntity.ok(ApiResponse.ok("Service ended", svc.terminateServiceRequest(requestId, residentId)));
    }
    @GetMapping("/provider/{providerId}/history")
    public ResponseEntity<ApiResponse<?>> providerHistory(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.ok("History", svc.getProviderHistory(providerId)));
    }
    @GetMapping("/resident/{residentId}/work-requests")
    public ResponseEntity<ApiResponse<?>> residentWork(@PathVariable Long residentId) {
        return ResponseEntity.ok(ApiResponse.ok("Work requests", svc.getResidentWorkRequests(residentId)));
    }
    @GetMapping("/resident/{residentId}/service-requests")
    public ResponseEntity<ApiResponse<?>> residentService(@PathVariable Long residentId) {
        return ResponseEntity.ok(ApiResponse.ok("Service requests", svc.getResidentServiceRequests(residentId)));
    }
    @GetMapping("/maids/approved")
    public ResponseEntity<ApiResponse<?>> approvedMaids() {
        return ResponseEntity.ok(ApiResponse.ok("Approved maids", svc.getApprovedMaids()));
    }
    // Resident cancels guest entry
    @PutMapping("/guest-entry/{entryId}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelGuestEntry(@PathVariable Long entryId, @RequestParam Long residentId) {
        svc.cancelGuestEntry(entryId, residentId);
        return ResponseEntity.ok(ApiResponse.ok("Guest entry cancelled", null));
    }
}
