package com.resiguard.resiguard.dto.request;
import com.resiguard.resiguard.model.enums.ServiceCategory;
import com.resiguard.resiguard.model.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    @NotBlank public String name;
    @Email @NotBlank public String email;
    @NotBlank @Size(min=6) public String password;
    @NotBlank public String phone;
    @NotNull public UserRole role;
    public String flatNumber;
    public String buildingName;
    public String address;
    public String idProofNumber;
    public ServiceCategory category;
    public String businessName;
    public String licenseNumber;
    public String badgeNumber;
    public String shift;
    public String employeeId;
    public String department;
    public String vehicleNumber;
    public String purposeOfVisit;
}
