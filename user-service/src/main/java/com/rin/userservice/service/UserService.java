package com.rin.userservice.service;

import com.rin.userservice.entity.User;
import com.rin.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Cacheable("allUsers")
    public List<User> getAllUsers() {
        System.out.println("⏳ Querying DB...");
        return userRepository.findAll();
    }

    @Cacheable(value = "userById", key = "#id")
    public User getUserById(Long id) {
        System.out.println("⏳ Querying DB for user id: " + id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User ensureUserExistsFromToken(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // sub
        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    // Tạo mới user nếu chưa có
                    User user = new User();
                    user.setKeycloakId(keycloakId);
                    user.setEmail(jwt.getClaim("email"));
                    user.setName(jwt.getClaim("preferred_username"));
                    return userRepository.save(user);
                });
    }


}
