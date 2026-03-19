package com.resiguard.resiguard.model;

import com.resiguard.resiguard.model.enums.EntryLogType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "entry_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EntryLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "guest_entry_id") private GuestEntry guestEntry;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "maid_id") private Maid maid;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "guard_id", nullable = false) private SecurityGuard guard;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EntryLogType logType;
    private String notes;
    private LocalDateTime loggedAt = LocalDateTime.now();
}
