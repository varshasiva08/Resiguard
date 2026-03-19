package com.resiguard.resiguard.model;

import com.resiguard.resiguard.model.enums.ServiceRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "service_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ServiceRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "resident_id", nullable = false) private Resident resident;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "service_provider_id", nullable = false) private ServiceProvider serviceProvider;
    private String description;
    private String preferredDateTime;
    @Enumerated(EnumType.STRING) private ServiceRequestStatus status = ServiceRequestStatus.PENDING;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private String rejectionReason;
}
