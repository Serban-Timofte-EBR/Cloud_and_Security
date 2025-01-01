package eu.learn.ro.controller;

import eu.learn.ro.model.User;
import eu.learn.ro.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return authService.loginUser(user);
    }
}
