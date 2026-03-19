package com.resiguard.resiguard.controller;
import com.resiguard.resiguard.decorator.ProfileEnrichmentService;
import com.resiguard.resiguard.dto.response.ApiResponse;
import com.resiguard.resiguard.exception.ResourceNotFoundException;
import com.resiguard.resiguard.model.User;
import com.resiguard.resiguard.repository.MaidRepository;
import com.resiguard.resiguard.repository.ServiceProviderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/profile")
public class ProfileController {
    private final MaidRepository maidRepo;
    private final ServiceProviderRepository spRepo;
    private final ProfileEnrichmentService enrichmentService;
    public ProfileController(MaidRepository maidRepo, ServiceProviderRepository spRepo, ProfileEnrichmentService enrichmentService) {
        this.maidRepo = maidRepo; this.spRepo = spRepo; this.enrichmentService = enrichmentService;
    }
    @GetMapping("/maid/{id}") public ResponseEntity<ApiResponse<?>> maid(@PathVariable Long id) {
        User u = maidRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Maid not found"));
        return ResponseEntity.ok(ApiResponse.ok("Profile", enrichmentService.enrich(u)));
    }
    @GetMapping("/provider/{id}") public ResponseEntity<ApiResponse<?>> provider(@PathVariable Long id) {
        User u = spRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        return ResponseEntity.ok(ApiResponse.ok("Profile", enrichmentService.enrich(u)));
    }
}
