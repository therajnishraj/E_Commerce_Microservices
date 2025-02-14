package com.microservice.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
/**
 * RouteValidator is responsible for determining whether a request requires authentication.
 *
 * It defines a list of open API endpoints that do not require authentication.
 * The `isSecured` predicate checks if the incoming request is for a secured route.
 *
 * Key Features:
 * - Maintains a list of open (public) API endpoints.
 * - Provides a predicate to determine if authentication is required.
 * - Works as a utility in the API Gateway for security filtering.
 *
 * Usage:
 * - The `isSecured` predicate is used in the authentication filter to bypass
 *   authentication for open endpoints.
 * - If a request URI is not in the open endpoints list, authentication is enforced.
 *
 * @author Rajnish Raj
 */
@Component
public class RouteValidator {
    /**
     * List of API endpoints that do not require authentication.
     * These endpoints are accessible without a valid JWT token.
     */
    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/token",
            "/eureka"
    );

    /**
     * Predicate to check if a request requires authentication.
     *
     * - Returns `true` if the request URI is **not** in the open API endpoints list (secured).
     * - Returns `false` if the request URI **matches** an open API endpoint (public access).
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
