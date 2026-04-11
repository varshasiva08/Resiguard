package com.resiguard.resiguard.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DecisionRequest {
    @NotNull public Boolean accepted;
    public String reason;
}
