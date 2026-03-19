package com.resiguard.resiguard.dto.request;
import com.resiguard.resiguard.model.enums.NotificationType;
import com.resiguard.resiguard.model.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationRequest {
    @NotBlank public String title;
    @NotBlank public String message;
    @NotNull public NotificationType type;
    public Long recipientId;
    public UserRole targetRole;
}
