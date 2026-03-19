package com.resiguard.resiguard.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GuestEntryDecisionRequest {
    @NotNull public Boolean approved;
    public String rejectionReason;
}
