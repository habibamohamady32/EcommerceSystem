package com.ecomfurniture.ecomsys.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "secretKey"; //need to rplace this
    public static String generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(algorithm);
    }
    public boolean validateToken(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            String extractedUsername = JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();

            return extractedUsername != null && extractedUsername.equals(username);
        } catch (Exception e) {
            return false;
        }
    }
    public String extractUsername(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();
    }
}
