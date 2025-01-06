package eu.learn.ro.cloudvault.controller;

import eu.learn.ro.cloudvault.dto.LoginRequest;
import eu.learn.ro.cloudvault.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login request received for username: " + loginRequest.getUsername());

        // Simulate authentication logic
        if ("password123".equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            System.out.println("Generated token: " + token);
            return ResponseEntity.ok(token);
        } else {
            System.out.println("Invalid credentials for username: " + loginRequest.getUsername());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}