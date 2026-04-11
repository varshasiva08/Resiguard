package com.resiguard.resiguard.decorator;
import com.resiguard.resiguard.model.User;
import java.util.Map;
public interface WorkerProfileDecorator {
    Map<String, Object> decorate(User user, Map<String, Object> profile);
}
