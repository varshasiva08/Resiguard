package com.resiguard.resiguard.service;
import com.resiguard.resiguard.dto.request.RatingRequest;
import com.resiguard.resiguard.model.Rating;
import java.util.List;
public interface RatingService {
    Rating rate(Long residentId, RatingRequest req);
    List<Rating> getMaidRatings(Long maidId);
    List<Rating> getProviderRatings(Long providerId);
}
