package com.microservice.userapp.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SignatureException;

/**
 * JwtUtil is a utility class for handling JWT token validation.
 *
 * This class:
 * - Validates JWT tokens to ensure they are correctly signed and not expired.
 * - Handles different JWT-related exceptions.
 * - Uses a secret key for token verification.
 *
 * Usage:
 * - Call {@code validateToken(token)} to check if a token is valid.
 * - Throws an appropriate exception if the token is invalid.
 *
 * @author Rajnish Raj
 */
@Component
public class JwtUtil {

    // Secret key used for JWT signing and verification (Base64 encoded)
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token to validate.
     * @throws ExpiredJwtException if the token has expired.
     * @throws MalformedJwtException if the token is incorrectly structured.
     * @throws UnsupportedJwtException if the token uses an unsupported format.
     * @throws JwtException for any other issues related to JWT processing.
     */
    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        }catch (Exception e) {
            System.out.println("Expired JWT token: " + e.getMessage());
            throw new RuntimeException("Expired JWT token", e);
        }
    }


    /**
     * Retrieves the signing key used for JWT validation.
     *
     * @return The secret key as a {@link Key} object.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
