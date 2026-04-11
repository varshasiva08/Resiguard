package com.resiguard.resiguard.strategy;
import com.resiguard.resiguard.model.Maid;
import com.resiguard.resiguard.model.enums.ProfileStatus;
import com.resiguard.resiguard.repository.MaidRepository;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class MaidSearchByRatingStrategy implements SearchStrategy<Maid> {
    private final MaidRepository maidRepo;
    public MaidSearchByRatingStrategy(MaidRepository maidRepo) { this.maidRepo = maidRepo; }
    @Override public List<Maid> search(String query) {
        return maidRepo.findByStatus(ProfileStatus.ACTIVE).stream()
                .sorted(Comparator.comparingDouble(Maid::getAverageRating).reversed())
                .collect(Collectors.toList());
    }
}
