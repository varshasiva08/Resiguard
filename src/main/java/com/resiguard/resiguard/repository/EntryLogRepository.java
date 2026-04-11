package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.EntryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {
    List<EntryLog> findByGuestEntryId(Long guestEntryId);
    List<EntryLog> findByGuardId(Long guardId);
}
