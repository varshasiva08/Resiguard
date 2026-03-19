package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.Maid;
import com.resiguard.resiguard.model.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface MaidRepository extends JpaRepository<Maid, Long> {
    Optional<Maid> findByEmail(String email);
    List<Maid> findByStatus(ProfileStatus status);
    List<Maid> findByNameContainingIgnoreCase(String name);
}
