//package com.rin.userservice.controller;
//
//
//import com.rin.userservice.entity.User;
//import com.rin.userservice.repository.UserRepository;
//import com.rin.userservice.service.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final UserRepository userRepo;
//    private final JwtService jwtService;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
//        String email = body.get("email");
//        String password = body.get("password");
//
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
//        }
//
//        String token = jwtService.generateToken(user);
//        return ResponseEntity.ok(Map.of("token", token));
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
//        String email = body.get("email");
//        String password = body.get("password");
//        String name = body.get("name");
//
//        if (userRepo.findByEmail(email).isPresent()) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Email already in use"));
//        }
//
//
//        User newUser = new User();
//        newUser.setEmail(email);
//        newUser.setPassword(passwordEncoder.encode(password));
//        newUser.setName(name);
//
//        userRepo.save(newUser);
//
//        String token = jwtService.generateToken(newUser);
//        return ResponseEntity.ok(Map.of("token", token));
//    }
//}
