package com.resiguard.resiguard.controller;
import com.resiguard.resiguard.dto.response.ApiResponse;
import com.resiguard.resiguard.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notifService;
    public NotificationController(NotificationService notifService) { this.notifService = notifService; }
    @GetMapping("/{userId}") public ResponseEntity<ApiResponse<?>> getAll(@PathVariable Long userId) { return ResponseEntity.ok(ApiResponse.ok("Notifications", notifService.getAll(userId))); }
    @GetMapping("/{userId}/unread") public ResponseEntity<ApiResponse<?>> unread(@PathVariable Long userId) { return ResponseEntity.ok(ApiResponse.ok("Unread", notifService.getUnread(userId))); }
    @GetMapping("/{userId}/unread/count") public ResponseEntity<ApiResponse<?>> count(@PathVariable Long userId) { return ResponseEntity.ok(ApiResponse.ok("Count", notifService.countUnread(userId))); }
    @PutMapping("/{id}/read") public ResponseEntity<ApiResponse<?>> markRead(@PathVariable Long id) { notifService.markAsRead(id); return ResponseEntity.ok(ApiResponse.ok("Marked read", null)); }
}
