package com.microservice.userapp.filter;

import com.microservice.userapp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import java.io.IOException;
/**
 * MyOncePerRequestFilter is a custom authentication filter that ensures
 * each request is processed only once per request lifecycle.
 *
 * This filter:
 * - Extracts the JWT token from the Authorization header.
 * - Validates the token using {@link JwtUtil}.
 * - Rejects requests with missing or invalid tokens by returning an error response.
 *
 * Usage:
 * - This filter is automatically applied to incoming requests due to the {@code @Component} annotation.
 * - It ensures authentication before allowing the request to proceed.
 *
 * @author Rajnish Raj
 */
@Component
public class MyOncePerRequestFilter extends OncePerRequestFilter {


    @Autowired
    JwtUtil jwtUtil;

    /**
     * Intercepts incoming requests to validate the JWT token.
     *
     * @param request the incoming HTTP request.
     * @param response the outgoing HTTP response.
     * @param filterChain the filter chain to proceed with the request if valid.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException if an I/O error occurs.
     */
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

    /**
     * Sends an error response with a specified message and HTTP status code.
     *
     * @param response the HTTP response.
     * @param message the error message.
     * @param statusCode the HTTP status code.
     * @throws IOException if an I/O error occurs while writing the response.
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

}
