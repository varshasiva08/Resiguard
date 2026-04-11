package com.resiguard.resiguard.factory;

import com.resiguard.resiguard.dto.request.RegisterRequest;
import com.resiguard.resiguard.model.*;
import com.resiguard.resiguard.model.enums.ProfileStatus;
import com.resiguard.resiguard.model.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    private final PasswordEncoder encoder;
    public UserFactory(PasswordEncoder encoder) { this.encoder = encoder; }

    public User createUser(RegisterRequest req) {
        String pwd = encoder.encode(req.password);
        return switch (req.role) {
            case RESIDENT -> { Resident r = new Resident(); setBase(r, req, pwd, UserRole.RESIDENT);
                r.setFlatNumber(req.flatNumber); r.setBuildingName(req.buildingName); yield r; }
            case MAID -> { Maid m = new Maid(); setBase(m, req, pwd, UserRole.MAID);
                m.setAddress(req.address); m.setIdProofNumber(req.idProofNumber);
                m.setStatus(ProfileStatus.PENDING); yield m; }
            case SERVICE_PROVIDER -> { ServiceProvider sp = new ServiceProvider();
                setBase(sp, req, pwd, UserRole.SERVICE_PROVIDER);
                sp.setCategory(req.category); sp.setBusinessName(req.businessName);
                sp.setLicenseNumber(req.licenseNumber); sp.setStatus(ProfileStatus.PENDING); yield sp; }
            case SECURITY_GUARD -> { SecurityGuard sg = new SecurityGuard();
                setBase(sg, req, pwd, UserRole.SECURITY_GUARD);
                sg.setBadgeNumber(req.badgeNumber); sg.setShift(req.shift); yield sg; }
            case ADMIN -> { Administrator a = new Administrator();
                setBase(a, req, pwd, UserRole.ADMIN);
                a.setEmployeeId(req.employeeId); a.setDepartment(req.department); yield a; }
            case GUEST -> { Guest g = new Guest(); setBase(g, req, pwd, UserRole.GUEST);
                g.setVehicleNumber(req.vehicleNumber); g.setPurposeOfVisit(req.purposeOfVisit); yield g; }
        };
    }
    private void setBase(User u, RegisterRequest req, String pwd, UserRole role) {
        u.setName(req.name); u.setEmail(req.email); u.setPassword(pwd);
        u.setPhone(req.phone); u.setRole(role);
    }
}
