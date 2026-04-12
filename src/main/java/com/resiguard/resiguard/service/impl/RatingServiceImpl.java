package com.resiguard.resiguard.service.impl;

import com.resiguard.resiguard.dto.request.RatingRequest;
import com.resiguard.resiguard.exception.BadRequestException;
import com.resiguard.resiguard.exception.ResourceNotFoundException;
import com.resiguard.resiguard.model.*;
import com.resiguard.resiguard.model.enums.NotificationType;
import com.resiguard.resiguard.repository.*;
import com.resiguard.resiguard.service.NotificationService;
import com.resiguard.resiguard.service.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepo;
    private final ResidentRepository residentRepo;
    private final MaidRepository maidRepo;
    private final ServiceProviderRepository spRepo;
    private final NotificationService notifService;

    public RatingServiceImpl(RatingRepository ratingRepo, ResidentRepository residentRepo,
                              MaidRepository maidRepo, ServiceProviderRepository spRepo,
                              NotificationService notifService) {
        this.ratingRepo = ratingRepo; this.residentRepo = residentRepo;
        this.maidRepo = maidRepo; this.spRepo = spRepo; this.notifService = notifService;
    }

    @Override
    public Rating rate(Long residentId, RatingRequest req) {
        if (req.maidId == null && req.serviceProviderId == null)
            throw new BadRequestException("Specify maidId or serviceProviderId");
        Resident resident = residentRepo.findById(residentId).orElseThrow(() -> new ResourceNotFoundException("Resident not found"));
        Rating rating = new Rating();
        rating.setResident(resident); rating.setScore(req.score); rating.setComment(req.comment);
        if (req.maidId != null) {
            if (ratingRepo.existsByResidentIdAndMaidId(residentId, req.maidId))
                throw new BadRequestException("Already rated this maid");
            Maid maid = maidRepo.findById(req.maidId).orElseThrow(() -> new ResourceNotFoundException("Maid not found"));
            rating.setMaid(maid); maid.recalculateRating(req.score); maidRepo.save(maid);
            // Fix 6: detailed notification with score and comment and updated average
            String msg = resident.getName() + " gave you " + req.score + "/5 stars."
                + (req.comment != null && !req.comment.isBlank() ? " Feedback: \"" + req.comment + "\"" : "")
                + " Your new average rating: " + String.format("%.1f", maid.getAverageRating()) + "/5 (" + maid.getTotalRatings() + " ratings).";
            notifService.sendToUser(maid.getId(), "New Rating Received", msg, NotificationType.RATING_RECEIVED);
        } else {
            if (ratingRepo.existsByResidentIdAndServiceProviderId(residentId, req.serviceProviderId))
                throw new BadRequestException("Already rated this provider");
            ServiceProvider sp = spRepo.findById(req.serviceProviderId).orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
            rating.setServiceProvider(sp); sp.recalculateRating(req.score); spRepo.save(sp);
            // Fix 6: detailed notification with score and comment and updated average
            String msg = resident.getName() + " gave you " + req.score + "/5 stars."
                + (req.comment != null && !req.comment.isBlank() ? " Feedback: \"" + req.comment + "\"" : "")
                + " Your new average rating: " + String.format("%.1f", sp.getAverageRating()) + "/5 (" + sp.getTotalRatings() + " ratings).";
            notifService.sendToUser(sp.getId(), "New Rating Received", msg, NotificationType.RATING_RECEIVED);
        }
        return ratingRepo.save(rating);
    }
    @Override public List<Rating> getMaidRatings(Long maidId) { return ratingRepo.findByMaidId(maidId); }
    @Override public List<Rating> getProviderRatings(Long providerId) { return ratingRepo.findByServiceProviderId(providerId); }
    @Override public List<Rating> getRatingsByResident(Long residentId) { return ratingRepo.findByResidentId(residentId); }
}
