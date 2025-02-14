package com.microservice.orderapp.filter;

import com.microservice.orderapp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * MyOncePerRequestFilter is a custom filter that runs once per request and is responsible for
 * validating the JWT token before allowing access to protected resources.
 *
 * This filter extracts the Authorization token from the request header, validates it,
 * and rejects unauthorized requests by sending an appropriate error response.
 *
 * Usage:
 * - This filter runs before processing any request.
 * - It checks for the presence of the JWT token in the Authorization header.
 * - If the token is missing or invalid, it responds with an HTTP 401 Unauthorized status.
 * - If the token is valid, the request proceeds to the next filter in the chain.
 *
 * Prerequisites:
 * - The client must include a valid JWT token in the `Authorization` header as `Bearer <token>`.
 * - The `JwtUtil` class should provide the token validation logic.
 *
 * @author Rajnish Raj
 */
@Component
public class MyOncePerRequestFilter extends OncePerRequestFilter {


    @Autowired
    JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isEmpty()) {
            sendErrorResponse(response, "Missing JWT token", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            jwtUtil.validateToken(token.substring(7));
        } catch (Exception e) {
            sendErrorResponse(response, "Invalid JWT token: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request,response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

}
