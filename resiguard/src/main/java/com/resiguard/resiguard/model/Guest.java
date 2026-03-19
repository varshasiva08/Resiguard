package com.resiguard.resiguard.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "guests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Guest extends User {
    private String vehicleNumber;
    private String purposeOfVisit;
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<GuestEntry> guestEntries = new ArrayList<>();
}
