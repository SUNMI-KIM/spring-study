package jwt.registerLogin.Repository;

import jwt.registerLogin.Domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<String, User> userRepository = new HashMap<>();

    public void save(User user) {
        userRepository.put(user.getEmail(), user);
    }
    public User find(String email) {
        return userRepository.get(email);
    }

    public Map<String, User> findAll() {
        return userRepository;
    }
}
