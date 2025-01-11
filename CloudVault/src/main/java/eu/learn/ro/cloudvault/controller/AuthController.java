package eu.learn.ro.cloudvault.controller;

import eu.learn.ro.cloudvault.dto.LoginRequest;
import eu.learn.ro.cloudvault.dto.RegisterRequest;
import eu.learn.ro.cloudvault.model.User;
import eu.learn.ro.cloudvault.repository.UserRepository;
import eu.learn.ro.cloudvault.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login request received for username: " + loginRequest.getUsername());

        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername(), List.of(user.getRole()));
            System.out.println("Generated token: " + token);
            return ResponseEntity.ok(token);
        } else {
            System.out.println("Invalid credentials for username: " + loginRequest.getUsername());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("Register request received for username: " + registerRequest.getUsername());

        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            return ResponseEntity.status(409).body("User already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword()); // TODO: Hash password
        user.setRole(registerRequest.getRole());
        userRepository.save(user);

        System.out.println("User registered successfully: " + user.getUsername());
        return ResponseEntity.ok("User registered successfully!");
    }
}