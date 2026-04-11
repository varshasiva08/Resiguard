package com.resiguard.resiguard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "ratings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_id", nullable = false)
    @JsonIgnoreProperties({"guestEntries","workRequests","serviceRequests","ratingsGiven","notifications","password"})
    private Resident resident;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maid_id")
    @JsonIgnoreProperties({"workRequests","ratingsReceived","entryLogs","notifications","password"})
    private Maid maid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id")
    @JsonIgnoreProperties({"serviceRequests","ratingsReceived","notifications","password"})
    private ServiceProvider serviceProvider;

    @Column(nullable = false) private Integer score;
    private String comment;
    private LocalDateTime ratedAt = LocalDateTime.now();
}
