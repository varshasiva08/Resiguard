package com.resiguard.resiguard.config;

import com.resiguard.resiguard.model.*;
import com.resiguard.resiguard.model.enums.*;
import com.resiguard.resiguard.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.count() > 0) return;
            Administrator admin = new Administrator();
            admin.setName("Admin"); admin.setEmail("admin@resiguard.com");
            admin.setPassword(encoder.encode("admin123")); admin.setPhone("9000000001");
            admin.setRole(UserRole.ADMIN); admin.setEmployeeId("EMP001"); admin.setDepartment("Admin");
            userRepo.save(admin);

            Resident resident = new Resident();
            resident.setName("Rahul Sharma"); resident.setEmail("rahul@resiguard.com");
            resident.setPassword(encoder.encode("password123")); resident.setPhone("9000000002");
            resident.setRole(UserRole.RESIDENT); resident.setFlatNumber("A-101"); resident.setBuildingName("Sunrise Apts");
            userRepo.save(resident);

            Guest guest = new Guest();
            guest.setName("Priya Visitor"); guest.setEmail("priya@example.com");
            guest.setPassword(encoder.encode("password123")); guest.setPhone("9000000003");
            guest.setRole(UserRole.GUEST); guest.setPurposeOfVisit("Family Visit");
            userRepo.save(guest);

            Maid maid = new Maid();
            maid.setName("Sunita Devi"); maid.setEmail("sunita@resiguard.com");
            maid.setPassword(encoder.encode("password123")); maid.setPhone("9000000004");
            maid.setRole(UserRole.MAID); maid.setAddress("123 Main Street");
            maid.setIdProofNumber("AADHAR-1234567890"); maid.setStatus(ProfileStatus.ACTIVE);
            userRepo.save(maid);

            ServiceProvider plumber = new ServiceProvider();
            plumber.setName("Ramesh Plumbers"); plumber.setEmail("ramesh@resiguard.com");
            plumber.setPassword(encoder.encode("password123")); plumber.setPhone("9000000005");
            plumber.setRole(UserRole.SERVICE_PROVIDER); plumber.setCategory(ServiceCategory.PLUMBER);
            plumber.setBusinessName("Ramesh Plumbing"); plumber.setLicenseNumber("LIC-001"); plumber.setStatus(ProfileStatus.ACTIVE);
            userRepo.save(plumber);

            SecurityGuard guard = new SecurityGuard();
            guard.setName("Vijay Guard"); guard.setEmail("vijay@resiguard.com");
            guard.setPassword(encoder.encode("password123")); guard.setPhone("9000000006");
            guard.setRole(UserRole.SECURITY_GUARD); guard.setBadgeNumber("BADGE-001"); guard.setShift("Day");
            userRepo.save(guard);

            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║   ResiGuard — Demo users seeded       ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║ admin@resiguard.com   / admin123      ║");
            System.out.println("║ rahul@resiguard.com   / password123   ║");
            System.out.println("║ priya@example.com     / password123   ║");
            System.out.println("║ sunita@resiguard.com  / password123   ║");
            System.out.println("║ ramesh@resiguard.com  / password123   ║");
            System.out.println("║ vijay@resiguard.com   / password123   ║");
            System.out.println("╚═══════════════════════════════════════╝");
        };
    }
}
