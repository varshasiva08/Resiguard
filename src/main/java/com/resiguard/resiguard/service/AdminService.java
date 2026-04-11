package com.resiguard.resiguard.service;
import com.resiguard.resiguard.model.User;
import com.resiguard.resiguard.model.enums.UserRole;
import java.util.List;
import java.util.Map;
public interface AdminService {
    User approveProfile(Long userId);
    User rejectProfile(Long userId, String reason);
    List<User> getPendingProfiles();
    List<User> getAllUsers();
    List<User> getUsersByRole(UserRole role);
    void deactivateUser(Long userId);
    Map<String, Object> generateReport();
}
