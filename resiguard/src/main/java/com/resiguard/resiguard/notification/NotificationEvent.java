package com.resiguard.resiguard.notification;
import com.resiguard.resiguard.model.enums.NotificationType;
import lombok.*;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class NotificationEvent {
    private Long recipientId;
    private String title;
    private String message;
    private NotificationType type;
}
