package com.resiguard.resiguard.service.impl;

import com.resiguard.resiguard.exception.ResourceNotFoundException;
import com.resiguard.resiguard.model.User;
import com.resiguard.resiguard.model.enums.NotificationType;
import com.resiguard.resiguard.model.enums.UserRole;
import com.resiguard.resiguard.notification.NotificationEvent;
import com.resiguard.resiguard.notification.NotificationPublisher;
import com.resiguard.resiguard.model.Notification;
import com.resiguard.resiguard.repository.NotificationRepository;
import com.resiguard.resiguard.repository.UserRepository;
import com.resiguard.resiguard.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifRepo;
    private final UserRepository userRepo;
    private final NotificationPublisher publisher;

    public NotificationServiceImpl(NotificationRepository notifRepo, UserRepository userRepo,
                                    NotificationPublisher publisher) {
        this.notifRepo = notifRepo; this.userRepo = userRepo; this.publisher = publisher;
    }

    @Override
    public void sendToUser(Long userId, String title, String message, NotificationType type) {
        publisher.publish(new NotificationEvent(userId, title, message, type));
    }

    @Override
    public void broadcast(String title, String message, NotificationType type,
                           Long recipientId, UserRole targetRole) {
        List<User> users;
        if (recipientId != null) {
            users = List.of(userRepo.findById(recipientId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        } else if (targetRole != null) {
            users = userRepo.findByRole(targetRole);
        } else {
            users = userRepo.findAll();
        }
        users.forEach(u -> publisher.publish(new NotificationEvent(u.getId(), title, message, type)));
    }

    @Override public List<Notification> getAll(Long userId) {
        return notifRepo.findByRecipientIdOrderByCreatedAtDesc(userId);
    }
    @Override public List<Notification> getUnread(Long userId) {
        return notifRepo.findByRecipientIdAndIsReadFalse(userId);
    }
    @Override public void markAsRead(Long notificationId) {
        Notification n = notifRepo.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        n.setRead(true); n.setReadAt(LocalDateTime.now()); notifRepo.save(n);
    }
    @Override public long countUnread(Long userId) {
        return notifRepo.countByRecipientIdAndIsReadFalse(userId);
    }
}
