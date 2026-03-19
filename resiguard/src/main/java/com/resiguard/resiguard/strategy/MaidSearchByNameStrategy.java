package com.resiguard.resiguard.strategy;
import com.resiguard.resiguard.model.Maid;
import com.resiguard.resiguard.repository.MaidRepository;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class MaidSearchByNameStrategy implements SearchStrategy<Maid> {
    private final MaidRepository maidRepo;
    public MaidSearchByNameStrategy(MaidRepository maidRepo) { this.maidRepo = maidRepo; }
    @Override public List<Maid> search(String query) {
        return maidRepo.findByNameContainingIgnoreCase(query);
    }
}
