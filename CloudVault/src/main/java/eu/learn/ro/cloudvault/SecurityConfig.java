package eu.learn.ro.cloudvault;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    private static final String secretKey = "MI]Qf!om{3nZ-1!)R[rwc4zse0%MX`wz";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN") // Only admins can access
                        .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN", "USER") // Admins and users
                        .requestMatchers("/api/public/**").permitAll() // Public access
                        .requestMatchers("/api/files/upload").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") // Upload: USER and ADMIN
                        .requestMatchers("/api/files/download/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") // Download: USER and ADMIN
                        .requestMatchers("/api/files/delete/**").hasAuthority("ROLE_ADMIN") // Delete: ADMIN only
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(authenticationManagerResolver()));

        // Debugging for security rules
        System.out.println("Security filter chain configured.");
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
        return request -> {
            String authHeader = request.getHeader("Authorization");
            System.out.println("Authorization header: " + authHeader);

            JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder());
            return new AuthenticationManager() {
                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    System.out.println("Authenticating token...");
                    return provider.authenticate(authentication);
                }
            };
        };
    }
}