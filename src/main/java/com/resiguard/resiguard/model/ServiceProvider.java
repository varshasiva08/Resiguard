package com.resiguard.resiguard.model;

import com.resiguard.resiguard.model.enums.ServiceCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "service_providers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ServiceProvider extends User {
    @Enumerated(EnumType.STRING) private ServiceCategory category;
    private String businessName;
    private String licenseNumber;
    private Double averageRating = 0.0;
    private Integer totalRatings = 0;
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ServiceRequest> serviceRequests = new ArrayList<>();
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratingsReceived = new ArrayList<>();

    public void recalculateRating(int newScore) {
        this.totalRatings++;
        this.averageRating = ((this.averageRating * (this.totalRatings - 1)) + newScore) / this.totalRatings;
    }
}
