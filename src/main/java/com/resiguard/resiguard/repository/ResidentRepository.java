package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByEmail(String email);
    // FIX 1: search by flat number for guest entry form
    List<Resident> findByFlatNumberContainingIgnoreCase(String flatNumber);
}
