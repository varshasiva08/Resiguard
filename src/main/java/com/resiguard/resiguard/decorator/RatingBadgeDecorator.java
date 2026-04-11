package com.resiguard.resiguard.decorator;
import com.resiguard.resiguard.model.Maid;
import com.resiguard.resiguard.model.ServiceProvider;
import com.resiguard.resiguard.model.User;
import org.springframework.stereotype.Component;
import java.util.Map;
@Component
public class RatingBadgeDecorator implements WorkerProfileDecorator {
    @Override public Map<String, Object> decorate(User user, Map<String, Object> profile) {
        double rating = 0.0; int total = 0;
        if (user instanceof Maid m) { rating = m.getAverageRating(); total = m.getTotalRatings(); }
        else if (user instanceof ServiceProvider sp) { rating = sp.getAverageRating(); total = sp.getTotalRatings(); }
        profile.put("averageRating", rating); profile.put("totalRatings", total);
        profile.put("badge", rating >= 4.5 ? "GOLD" : rating >= 3.5 ? "SILVER" : rating >= 2.5 ? "BRONZE" : "UNRATED");
        return profile;
    }
}
