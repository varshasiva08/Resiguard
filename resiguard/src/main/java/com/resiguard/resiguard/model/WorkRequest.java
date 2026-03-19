package com.resiguard.resiguard.model;

import com.resiguard.resiguard.model.enums.WorkRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "work_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WorkRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "resident_id", nullable = false) private Resident resident;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "maid_id", nullable = false) private Maid maid;
    private String description;
    private LocalDate startDate;
    private String workTimings;
    @Enumerated(EnumType.STRING) private WorkRequestStatus status = WorkRequestStatus.PENDING;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private String rejectionReason;
}
