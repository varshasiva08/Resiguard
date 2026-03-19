package com.resiguard.resiguard.decorator;
import com.resiguard.resiguard.model.User;
import org.springframework.stereotype.Component;
import java.util.LinkedHashMap;
import java.util.Map;
@Component
public class BaseWorkerProfile implements WorkerProfileDecorator {
    @Override public Map<String, Object> decorate(User user, Map<String, Object> profile) {
        Map<String, Object> base = new LinkedHashMap<>();
        base.put("id", user.getId()); base.put("name", user.getName());
        base.put("email", user.getEmail()); base.put("phone", user.getPhone());
        base.put("role", user.getRole()); base.put("status", user.getStatus());
        base.putAll(profile); return base;
    }
}
