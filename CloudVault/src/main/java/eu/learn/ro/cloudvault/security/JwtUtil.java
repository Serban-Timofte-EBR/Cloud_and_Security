package eu.learn.ro.cloudvault.security;

import eu.learn.ro.cloudvault.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {

    private final String secretKey = "MI]Qf!om{3nZ-1!)R[rwc4zse0%MX`wz";
    private final Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());


    public String generateToken(String username, List<Role> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.stream().map(role -> "ROLE_" + role.name()).toList()) // Prefix roles
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(key)
                .compact();
    }

    public String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}