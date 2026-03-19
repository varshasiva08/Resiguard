package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByEmail(String email);
}
