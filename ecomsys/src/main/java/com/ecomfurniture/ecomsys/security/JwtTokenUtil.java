package com.ecomfurniture.ecomsys.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private final String secretKey;

    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }
    public String generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 600_000)) //10 min
                .sign(algorithm);
    }
    public boolean validateToken(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String extractedUsername = JWT.require(algorithm).build().verify(token).getSubject();
            return extractedUsername != null && extractedUsername.equals(username);
        } catch (Exception e) {
            return false;
        }
    }
    public String extractEmail(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getSubject();
    }
}
