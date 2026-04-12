package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.GuestEntry;
import com.resiguard.resiguard.model.enums.GuestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
public interface GuestEntryRepository extends JpaRepository<GuestEntry, Long> {
    List<GuestEntry> findByResidentId(Long residentId);
    List<GuestEntry> findByGuestId(Long guestId);
    List<GuestEntry> findByStatus(GuestStatus status);
    Optional<GuestEntry> findByEntryPassCode(String passCode);

    // Find entries that have an ENTRY log but no EXIT log yet (currently on premises)
    @Query("SELECT DISTINCT ge FROM GuestEntry ge " +
           "WHERE ge.status = 'APPROVED' AND ge.passCodeExpired = false " +
           "AND EXISTS (SELECT l FROM EntryLog l WHERE l.guestEntry = ge AND l.logType = 'ENTRY') " +
           "AND NOT EXISTS (SELECT l FROM EntryLog l WHERE l.guestEntry = ge AND l.logType = 'EXIT')")
    List<GuestEntry> findActiveGuestsOnPremises();

    // Find approved entries with passcode not expired (for guest home page)
    List<GuestEntry> findByGuestIdAndStatusAndPassCodeExpired(Long guestId, GuestStatus status, boolean expired);
}
