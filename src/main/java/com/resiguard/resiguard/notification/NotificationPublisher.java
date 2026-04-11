package com.resiguard.resiguard.notification;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
@Component
public class NotificationPublisher {
    private final List<NotificationObserver> observers = new ArrayList<>();
    public void addObserver(NotificationObserver o) { observers.add(o); }
    public void publish(NotificationEvent event) { observers.forEach(o -> o.update(event)); }
}
