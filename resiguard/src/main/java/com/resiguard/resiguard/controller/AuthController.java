package com.resiguard.resiguard.controller;
import com.resiguard.resiguard.dto.request.LoginRequest;
import com.resiguard.resiguard.dto.request.RegisterRequest;
import com.resiguard.resiguard.dto.response.ApiResponse;
import com.resiguard.resiguard.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Registered successfully", authService.register(req)));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authService.login(req)));
    }
}
