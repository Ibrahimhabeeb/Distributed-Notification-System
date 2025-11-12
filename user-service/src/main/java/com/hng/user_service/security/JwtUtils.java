package com.hng.user_service.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final long expirationMills;

    public JwtUtils(
            @Value("${auth.token.jwtSecret}") String secret,
            @Value("${auth.token.expirationInMils}") long expirationMills
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMills = expirationMills;
    }


    public String generateToken(String userId, String email) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(email)
                .setIssuer("user-service")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationMills)))
                .addClaims(Map.of(
                        "userId", userId
                ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }


    public Claims extractClaims(String token) throws JwtException {
        return parseToken(token).getBody();
    }


    public boolean isExpired(String token) {
        Date exp = extractClaims(token).getExpiration();
        return exp.before(new Date());
    }
}
