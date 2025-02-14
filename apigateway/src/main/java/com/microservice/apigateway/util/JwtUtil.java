package com.microservice.apigateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
/**
 * JwtUtil is a utility class for handling JWT validation in the API Gateway.
 *
 * This class is responsible for validating JWT tokens to ensure they are properly signed and not tampered with.
 *
 * Key Features:
 * - Uses a predefined secret key for HMAC signing.
 * - Decodes and verifies JWT tokens using the `validateToken` method.
 * - Provides a helper method to retrieve the signing key.
 *
 * Usage:
 * - This utility is used in authentication filters to validate JWT tokens before processing requests.
 * - Ensures that incoming requests contain valid authentication credentials.
 *
 * @author Rajnish Raj
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing and validating JWT tokens.
     * It is encoded in Base64 and should be kept secure.
     */
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to be validated.
     * @throws io.jsonwebtoken.JwtException if the token is invalid or tampered with.
     */
    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }


    /**
     * Retrieves the signing key used to verify JWT tokens.
     *
     * @return The secret key used for JWT validation.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
