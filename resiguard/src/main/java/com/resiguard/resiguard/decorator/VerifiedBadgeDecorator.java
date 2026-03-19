package com.resiguard.resiguard.decorator;
import com.resiguard.resiguard.model.User;
import com.resiguard.resiguard.model.enums.ProfileStatus;
import org.springframework.stereotype.Component;
import java.util.Map;
@Component
public class VerifiedBadgeDecorator implements WorkerProfileDecorator {
    @Override public Map<String, Object> decorate(User user, Map<String, Object> profile) {
        profile.put("verified", user.getStatus() == ProfileStatus.ACTIVE);
        profile.put("verificationStatus", user.getStatus().name());
        return profile;
    }
}
