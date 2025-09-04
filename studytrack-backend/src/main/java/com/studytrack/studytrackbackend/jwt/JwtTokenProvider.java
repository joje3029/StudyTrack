package com.studytrack.studytrackbackend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private final SecretKey key;

    public JwtTokenProvider(
            @Value("${jwt.secret:mySecretKey}") String secret,
            @Value("${jwt.access-token-validity-in-seconds:3600}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity-in-seconds:604800}") long refreshTokenValidity) {
        this.secret = secret;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(String email, String role) {
        return createToken(email, role, accessTokenValidity);
    }

    public String createRefreshToken(String email, String role) {
        return createToken(email, role, refreshTokenValidity);
    }

    private String createToken(String email, String role, long validitySeconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validitySeconds * 1000);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        String role = (String) claims.get("role");
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        return new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            // TODO: Log exception
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
