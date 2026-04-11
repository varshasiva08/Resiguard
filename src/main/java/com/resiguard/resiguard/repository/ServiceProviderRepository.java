package com.resiguard.resiguard.repository;
import com.resiguard.resiguard.model.ServiceProvider;
import com.resiguard.resiguard.model.enums.ProfileStatus;
import com.resiguard.resiguard.model.enums.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    Optional<ServiceProvider> findByEmail(String email);
    List<ServiceProvider> findByStatus(ProfileStatus status);
    List<ServiceProvider> findByCategory(ServiceCategory category);
    List<ServiceProvider> findByCategoryAndStatus(ServiceCategory category, ProfileStatus status);
    List<ServiceProvider> findByNameContainingIgnoreCase(String name);
}
