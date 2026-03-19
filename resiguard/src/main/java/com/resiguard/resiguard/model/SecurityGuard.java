package com.resiguard.resiguard.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "security_guards")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SecurityGuard extends User {
    private String badgeNumber;
    private String shift;
    @OneToMany(mappedBy = "guard", cascade = CascadeType.ALL)
    private List<EntryLog> entryLogs = new ArrayList<>();
}
