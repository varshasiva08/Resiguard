package com.resiguard.resiguard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.resiguard.resiguard.model.enums.EntryLogType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "entry_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EntryLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_entry_id")
    @JsonIgnoreProperties({"guest","resident"})
    private GuestEntry guestEntry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maid_id")
    @JsonIgnoreProperties({"workRequests","ratingsReceived","entryLogs","notifications","password"})
    private Maid maid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guard_id", nullable = false)
    @JsonIgnoreProperties({"entryLogs","notifications","password"})
    private SecurityGuard guard;

    @Enumerated(EnumType.STRING) @Column(nullable = false) private EntryLogType logType;
    private String notes;
    private LocalDateTime loggedAt = LocalDateTime.now();
}
