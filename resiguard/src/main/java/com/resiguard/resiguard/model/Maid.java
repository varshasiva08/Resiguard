package com.resiguard.resiguard.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "maids")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Maid extends User {
    private String address;
    private String idProofNumber;
    private Double averageRating = 0.0;
    private Integer totalRatings = 0;
    @OneToMany(mappedBy = "maid", cascade = CascadeType.ALL)
    private List<WorkRequest> workRequests = new ArrayList<>();
    @OneToMany(mappedBy = "maid", cascade = CascadeType.ALL)
    private List<Rating> ratingsReceived = new ArrayList<>();
    @OneToMany(mappedBy = "maid", cascade = CascadeType.ALL)
    private List<EntryLog> entryLogs = new ArrayList<>();

    public void recalculateRating(int newScore) {
        this.totalRatings++;
        this.averageRating = ((this.averageRating * (this.totalRatings - 1)) + newScore) / this.totalRatings;
    }
}
