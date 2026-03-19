package com.resiguard.resiguard.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ServiceRequestDto {
    @NotNull public Long serviceProviderId;
    @NotBlank public String description;
    public String preferredDateTime;
}
