package com.resiguard.resiguard.service.impl;

import com.resiguard.resiguard.exception.BadRequestException;
import com.resiguard.resiguard.exception.ResourceNotFoundException;
import com.resiguard.resiguard.model.User;
import com.resiguard.resiguard.model.enums.NotificationType;
import com.resiguard.resiguard.model.enums.ProfileStatus;
import com.resiguard.resiguard.model.enums.UserRole;
import com.resiguard.resiguard.repository.*;
import com.resiguard.resiguard.service.AdminService;
import com.resiguard.resiguard.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service @Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepo;
    private final GuestEntryRepository guestEntryRepo;
    private final WorkRequestRepository workRequestRepo;
    private final ServiceRequestRepository serviceRequestRepo;
    private final RatingRepository ratingRepo;
    private final NotificationService notifService;

    public AdminServiceImpl(UserRepository userRepo, GuestEntryRepository guestEntryRepo,
                             WorkRequestRepository workRequestRepo, ServiceRequestRepository serviceRequestRepo,
                             RatingRepository ratingRepo, NotificationService notifService) {
        this.userRepo = userRepo; this.guestEntryRepo = guestEntryRepo;
        this.workRequestRepo = workRequestRepo; this.serviceRequestRepo = serviceRequestRepo;
        this.ratingRepo = ratingRepo; this.notifService = notifService;
    }

    @Override public User approveProfile(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getStatus() != ProfileStatus.PENDING) throw new BadRequestException("Not in PENDING state");
        user.setStatus(ProfileStatus.ACTIVE); User saved = userRepo.save(user);
        notifService.sendToUser(userId, "Profile Approved", "Your profile is verified. You can now login.", NotificationType.PROFILE_APPROVED);
        return saved;
    }
    @Override public User rejectProfile(Long userId, String reason) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(ProfileStatus.REJECTED); User saved = userRepo.save(user);
        notifService.sendToUser(userId, "Profile Rejected", "Rejected. Reason: " + reason, NotificationType.PROFILE_REJECTED);
        return saved;
    }
    @Override public List<User> getPendingProfiles() {
        return userRepo.findAll().stream().filter(u -> u.getStatus() == ProfileStatus.PENDING).collect(Collectors.toList());
    }
    @Override public List<User> getAllUsers() { return userRepo.findAll(); }
    @Override public List<User> getUsersByRole(UserRole role) { return userRepo.findByRole(role); }
    @Override public void deactivateUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(ProfileStatus.INACTIVE); userRepo.save(user);
    }
    @Override public Map<String, Object> generateReport() {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("totalUsers", userRepo.count());
        r.put("usersByRole", Arrays.stream(UserRole.values()).collect(Collectors.toMap(Enum::name, role -> userRepo.findByRole(role).size())));
        r.put("totalGuestEntries", guestEntryRepo.count());
        r.put("totalWorkRequests", workRequestRepo.count());
        r.put("totalServiceRequests", serviceRequestRepo.count());
        r.put("totalRatings", ratingRepo.count());
        r.put("pendingProfiles", getPendingProfiles().size());
        r.put("generatedAt", java.time.LocalDateTime.now().toString());
        return r;
    }
}
