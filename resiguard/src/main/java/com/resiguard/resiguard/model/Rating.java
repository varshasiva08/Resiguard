package com.resiguard.resiguard.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "ratings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "resident_id", nullable = false) private Resident resident;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "maid_id") private Maid maid;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "service_provider_id") private ServiceProvider serviceProvider;
    @Column(nullable = false) private Integer score;
    private String comment;
    private LocalDateTime ratedAt = LocalDateTime.now();
}
