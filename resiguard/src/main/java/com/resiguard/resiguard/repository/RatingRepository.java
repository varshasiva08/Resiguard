package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMaidId(Long maidId);
    List<Rating> findByServiceProviderId(Long providerId);
    List<Rating> findByResidentId(Long residentId);
    boolean existsByResidentIdAndMaidId(Long residentId, Long maidId);
    boolean existsByResidentIdAndServiceProviderId(Long residentId, Long providerId);
}
