package com.resiguard.resiguard.model;

import com.resiguard.resiguard.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "recipient_id", nullable = false) private User recipient;
    @Column(nullable = false) private String title;
    @Column(nullable = false, length = 1000) private String message;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private NotificationType type;
    private boolean isRead = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime readAt;
}
