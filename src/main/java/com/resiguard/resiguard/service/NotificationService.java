package com.resiguard.resiguard.service;
import com.resiguard.resiguard.model.Notification;
import com.resiguard.resiguard.model.enums.NotificationType;
import com.resiguard.resiguard.model.enums.UserRole;
import java.util.List;
public interface NotificationService {
    void sendToUser(Long userId, String title, String message, NotificationType type);
    void broadcast(String title, String message, NotificationType type, Long recipientId, UserRole targetRole);
    List<Notification> getAll(Long userId);
    List<Notification> getUnread(Long userId);
    void markAsRead(Long notificationId);
    long countUnread(Long userId);
}
