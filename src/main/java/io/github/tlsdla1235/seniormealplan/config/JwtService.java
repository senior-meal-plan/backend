package io.github.tlsdla1235.seniormealplan.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;                 // ★ Key 말고 이걸 사용

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;  // 256비트 이상 권장 (Base64)
    @Value("${jwt.exp-minutes:60}")
    private long expMinutes;

    private SecretKey secretKey;

    @PostConstruct
    void init() { secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); }

    public String generateToken(String subject, Map<String,Object> claims) {
        var now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expMinutes, ChronoUnit.MINUTES)))
                .signWith(secretKey)                 // ← 캐시 사용
                .compact();
    }

    public String extractSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)               // ← 캐시 사용
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
        try {
            var payload = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return user.getUsername().equals(payload.getSubject())
                    && payload.getExpiration().after(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return false;
        } catch (io.jsonwebtoken.JwtException e) { // 서명 불일치/변조 등
            return false;
        }
    }

}
