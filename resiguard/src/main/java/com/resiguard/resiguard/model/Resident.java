package com.resiguard.resiguard.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "residents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Resident extends User {
    @Column(nullable = false) private String flatNumber;
    @Column(nullable = false) private String buildingName;
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private List<GuestEntry> guestEntries = new ArrayList<>();
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private List<WorkRequest> workRequests = new ArrayList<>();
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private List<ServiceRequest> serviceRequests = new ArrayList<>();
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private List<Rating> ratingsGiven = new ArrayList<>();
}
