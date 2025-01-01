package eu.learn.ro.service;

import eu.learn.ro.model.User;
import eu.learn.ro.repository.UserRepository;
import eu.learn.ro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user
    public String registerUser(User user) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Hash the password and save the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "User registered successfully!";
    }

    // Login user and generate JWT
    public String loginUser(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate and return the JWT
        return jwtUtil.generateToken(user.getUsername());
    }
}