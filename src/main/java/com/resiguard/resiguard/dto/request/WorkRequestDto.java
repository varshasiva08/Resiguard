package com.resiguard.resiguard.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WorkRequestDto {
    @NotNull public Long maidId;
    @NotBlank public String description;
    public LocalDate startDate;
    public String workTimings;
}
