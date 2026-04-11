package com.resiguard.resiguard.strategy;
import com.resiguard.resiguard.model.User;
import java.util.List;
public interface SearchStrategy<T extends User> {
    List<T> search(String query);
}
