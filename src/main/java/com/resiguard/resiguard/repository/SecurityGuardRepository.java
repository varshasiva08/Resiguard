package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.SecurityGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SecurityGuardRepository extends JpaRepository<SecurityGuard, Long> {
    Optional<SecurityGuard> findByEmail(String email);
}
