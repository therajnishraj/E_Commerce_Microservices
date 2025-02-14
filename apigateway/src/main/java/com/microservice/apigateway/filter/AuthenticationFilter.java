package com.microservice.apigateway.filter;

import com.microservice.apigateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * AuthenticationFilter is a custom Gateway filter that validates JWT tokens
 * for secured API routes in the API Gateway.
 *
 * This filter intercepts incoming requests, checks for the Authorization header,
 * extracts the JWT token, and validates it using {@link JwtUtil}.
 * If the token is missing, incorrectly formatted, or invalid, the request is
 * rejected with an appropriate HTTP status code.
 *
 * Key Features:
 * - Ensures authentication for secured routes.
 * - Extracts and validates JWT tokens from the Authorization header.
 * - Returns error responses for missing or invalid tokens.
 * - Attaches the route ID to the exchange attributes for downstream processing.
 *
 * Usage:
 * - Automatically applied to routes that require authentication.
 * - Works within Spring Cloud Gateway to secure microservices.
 *
 * @author Rajnish Raj
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);


    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;


    public AuthenticationFilter() {
        super(Config.class);
    }

    /**
     * Applies the authentication filter logic to incoming requests.
     *
     * @param config The filter configuration (not used in this implementation).
     * @return A GatewayFilter that processes authentication for secured routes.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // Check if the request requires authentication
            if (validator.isSecured.test(exchange.getRequest())) {
                // Validate the presence of the Authorization header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return handleError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
                }
                // Validate the format of the Authorization header
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7); // Extract token
                } else {
                    return handleError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
                }

                // Validate the JWT token
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    return handleError(exchange, "Invalid token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
                }
            }
            Route route=exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
            exchange.getAttributes().put("routeId", route.getId());
            return chain.filter(exchange);
        };
    }

    /**
     * Creates a custom error response with a meaningful message.
     *
     * @param exchange the current server exchange
     * @param message the error message to return
     * @param status the HTTP status to return
     * @return a Mono<Void> that completes after writing the response
     */
    private Mono<Void> handleError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        log.info("error {}",message);
        String errorResponse = String.format("{\"error\": \"%s\"}", message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes())));
    }

    public static class Config {
        // Empty config class as needed by AbstractGatewayFilterFactory
    }
}
