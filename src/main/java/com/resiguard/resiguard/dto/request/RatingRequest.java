package com.resiguard.resiguard.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RatingRequest {
    public Long maidId;
    public Long serviceProviderId;
    @NotNull @Min(1) @Max(5) public Integer score;
    public String comment;
}
