package com.resiguard.resiguard.model;

import com.resiguard.resiguard.model.enums.GuestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "guest_entries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GuestEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "guest_id", nullable = false) private Guest guest;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "resident_id", nullable = false) private Resident resident;
    @Enumerated(EnumType.STRING) private GuestStatus status = GuestStatus.PENDING;
    private String purpose;
    private String entryPassCode;
    private LocalDateTime requestedAt = LocalDateTime.now();
    private LocalDateTime decidedAt;
    private String rejectionReason;
}
