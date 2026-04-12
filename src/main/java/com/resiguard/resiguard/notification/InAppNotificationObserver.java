package com.resiguard.resiguard.notification;

import com.resiguard.resiguard.model.Notification;
import com.resiguard.resiguard.model.User;
import com.resiguard.resiguard.repository.NotificationRepository;
import com.resiguard.resiguard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class InAppNotificationObserver implements NotificationObserver {
    private static final Logger log = LoggerFactory.getLogger(InAppNotificationObserver.class);
    private final NotificationRepository notifRepo;
    private final UserRepository userRepo;

    public InAppNotificationObserver(NotificationRepository notifRepo, UserRepository userRepo,
                                      NotificationPublisher publisher) {
        this.notifRepo = notifRepo; this.userRepo = userRepo;
        publisher.addObserver(this);
    }

    @Override
    public void update(NotificationEvent event) {
        Optional<User> user = userRepo.findById(event.getRecipientId());
        if (user.isEmpty()) return;
        Notification n = new Notification();
        n.setRecipient(user.get());
        n.setTitle(event.getTitle());
        n.setMessage(event.getMessage());
        n.setType(event.getType());
        n.setRead(false);
        notifRepo.save(n);
        log.info("[NOTIF] Saved for user {}: {}", event.getRecipientId(), event.getTitle());
    }
}
