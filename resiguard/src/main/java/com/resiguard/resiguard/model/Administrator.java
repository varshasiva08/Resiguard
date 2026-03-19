package com.resiguard.resiguard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "administrators")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Administrator extends User {
    private String employeeId;
    private String department;
}
