package com.resiguard.resiguard.controller;
import com.resiguard.resiguard.dto.request.RatingRequest;
import com.resiguard.resiguard.dto.response.ApiResponse;
import com.resiguard.resiguard.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/ratings")
public class RatingController {
    private final RatingService ratingService;
    public RatingController(RatingService ratingService) { this.ratingService = ratingService; }
    @PostMapping("/resident/{residentId}")
    public ResponseEntity<ApiResponse<?>> rate(@PathVariable Long residentId, @Valid @RequestBody RatingRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Rating submitted", ratingService.rate(residentId, req)));
    }
    @GetMapping("/maid/{maidId}")
    public ResponseEntity<ApiResponse<?>> maidRatings(@PathVariable Long maidId) {
        return ResponseEntity.ok(ApiResponse.ok("Ratings", ratingService.getMaidRatings(maidId)));
    }
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<?>> providerRatings(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.ok("Ratings", ratingService.getProviderRatings(providerId)));
    }
}
