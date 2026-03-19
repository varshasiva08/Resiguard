package com.resiguard.resiguard.dto.response;
import com.resiguard.resiguard.model.enums.UserRole;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    public String token;
    public String tokenType = "Bearer";
    public Long userId;
    public String name;
    public String email;
    public UserRole role;
}
