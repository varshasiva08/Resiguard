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
}
