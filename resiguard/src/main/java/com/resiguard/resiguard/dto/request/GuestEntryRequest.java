package com.resiguard.resiguard.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GuestEntryRequest {
    @NotNull public Long residentId;
    @NotBlank public String purpose;
}
