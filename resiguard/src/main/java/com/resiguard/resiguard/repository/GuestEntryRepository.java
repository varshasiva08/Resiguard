package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.GuestEntry;
import com.resiguard.resiguard.model.enums.GuestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface GuestEntryRepository extends JpaRepository<GuestEntry, Long> {
    List<GuestEntry> findByResidentId(Long residentId);
    List<GuestEntry> findByGuestId(Long guestId);
    List<GuestEntry> findByStatus(GuestStatus status);
    Optional<GuestEntry> findByEntryPassCode(String passCode);
}
