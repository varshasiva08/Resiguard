package com.resiguard.resiguard.notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
public class EmailNotificationObserver implements NotificationObserver {
    private static final Logger log = LoggerFactory.getLogger(EmailNotificationObserver.class);
    public EmailNotificationObserver(NotificationPublisher publisher) {
        publisher.addObserver(this);
    }
    @Override
    public void update(NotificationEvent event) {
        // Wire JavaMailSender here for production
        log.info("[EMAIL] userId={} | {}: {}", event.getRecipientId(), event.getTitle(), event.getMessage());
    }
}
