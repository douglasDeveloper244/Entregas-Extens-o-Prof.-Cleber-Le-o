package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.entity.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key;
    private final long jwtExpirationMs;
    private final long jwtRefreshExpirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration:86400000}") long jwtExpirationMs,
            @Value("${jwt.refresh-expiration:864000000}") long jwtRefreshExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }

    // ============================================================
    // ACCESS TOKEN
    // ============================================================
    public String generateToken(Usuario user) {
        return buildToken(user, jwtExpirationMs, "access");
    }

    // ============================================================
    // REFRESH TOKEN
    // ============================================================
    public String generateRefreshToken(Usuario user) {
        return buildToken(user, jwtRefreshExpirationMs, "refresh");
    }

    private String buildToken(Usuario user, long expiration, String type) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("type", type); // ESSENCIAL !!
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());
        if (user.getRestauranteId() != null) {
            claims.put("restauranteId", user.getRestauranteId());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ============================================================
    // EXTRAÇÃO DE CLAIMS
    // ============================================================
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractType(String token) {
        return extractClaim(token, c -> (String) c.get("type"));
    }

    public Long extractUserId(String token) {
        Object v = extractClaim(token, c -> c.get("userId"));
        if (v instanceof Integer)
            return ((Integer) v).longValue();
        if (v instanceof Long)
            return (Long) v;
        return null;
    }

    public boolean isTokenExpired(String token) {
        Date exp = extractClaim(token, Claims::getExpiration);
        return exp.before(new Date());
    }

    // ============================================================
    // VALIDAÇÃO
    // ============================================================
    public boolean validateToken(String token, Usuario user) {
        return validateTokenType(token, user, "access");
    }

    public boolean validateTokenType(String token, Usuario user, String expectedType) {
        try {
            Claims c = parseClaims(token);
            String username = c.getSubject();
            String type = (String) c.get("type");

            if (!username.equals(user.getEmail()))
                return false;
            if (isTokenExpired(token))
                return false;

            return type.equals(expectedType);

        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
