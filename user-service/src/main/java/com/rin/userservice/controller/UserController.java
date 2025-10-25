package com.rin.userservice.controller;


import com.rin.common.dto.ApiResponse;
import com.rin.common.exception.ErrorCode;
import com.rin.userservice.dto.UserDto;
import com.rin.userservice.entity.User;
import com.rin.userservice.repository.UserRepository;
import com.rin.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private  final UserService userService;
    ErrorCode errorCode = ErrorCode.RIN;
    @PostMapping
    @CacheEvict (value = "allUsers", allEntries = true)
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
    @GetMapping("/keycloak/{sub}")
    public UserDto getUserByKeycloakId(@PathVariable String sub, @AuthenticationPrincipal Jwt jwt) {
        //tự tạo user từ token
        User user = userRepository.findByKeycloakId(sub)
                .orElseGet(() -> userService.ensureUserExistsFromToken(jwt));

        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

}
