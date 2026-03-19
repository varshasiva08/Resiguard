package com.resiguard.resiguard.decorator;
import com.resiguard.resiguard.model.User;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Service
public class ProfileEnrichmentService {
    private final List<WorkerProfileDecorator> decorators;
    public ProfileEnrichmentService(BaseWorkerProfile base, RatingBadgeDecorator rating,
                                     VerifiedBadgeDecorator verified) {
        this.decorators = List.of(base, rating, verified);
    }
    public Map<String, Object> enrich(User user) {
        Map<String, Object> profile = new LinkedHashMap<>();
        for (WorkerProfileDecorator d : decorators) profile = d.decorate(user, profile);
        return profile;
    }
}
