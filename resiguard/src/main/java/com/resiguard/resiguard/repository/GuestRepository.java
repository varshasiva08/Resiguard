package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmail(String email);
}
