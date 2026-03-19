package com.resiguard.resiguard.controller;
import com.resiguard.resiguard.dto.request.NotificationRequest;
import com.resiguard.resiguard.dto.response.ApiResponse;
import com.resiguard.resiguard.model.enums.UserRole;
import com.resiguard.resiguard.service.AdminService;
import com.resiguard.resiguard.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final NotificationService notifService;
    public AdminController(AdminService adminService, NotificationService notifService) {
        this.adminService = adminService; this.notifService = notifService;
    }
    @GetMapping("/profiles/pending")
    public ResponseEntity<ApiResponse<?>> pending() { return ResponseEntity.ok(ApiResponse.ok("Pending", adminService.getPendingProfiles())); }
    @PutMapping("/profiles/{userId}/approve")
    public ResponseEntity<ApiResponse<?>> approve(@PathVariable Long userId) { return ResponseEntity.ok(ApiResponse.ok("Approved", adminService.approveProfile(userId))); }
    @PutMapping("/profiles/{userId}/reject")
    public ResponseEntity<ApiResponse<?>> reject(@PathVariable Long userId, @RequestParam String reason) { return ResponseEntity.ok(ApiResponse.ok("Rejected", adminService.rejectProfile(userId, reason))); }
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> users() { return ResponseEntity.ok(ApiResponse.ok("Users", adminService.getAllUsers())); }
    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse<?>> byRole(@PathVariable UserRole role) { return ResponseEntity.ok(ApiResponse.ok("Users", adminService.getUsersByRole(role))); }
    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<ApiResponse<?>> deactivate(@PathVariable Long userId) { adminService.deactivateUser(userId); return ResponseEntity.ok(ApiResponse.ok("Deactivated", null)); }
    @GetMapping("/report")
    public ResponseEntity<ApiResponse<?>> report() { return ResponseEntity.ok(ApiResponse.ok("Report", adminService.generateReport())); }
    @PostMapping("/notify")
    public ResponseEntity<ApiResponse<?>> notify(@Valid @RequestBody NotificationRequest req) {
        notifService.broadcast(req.title, req.message, req.type, req.recipientId, req.targetRole);
        return ResponseEntity.ok(ApiResponse.ok("Sent", null));
    }
}
