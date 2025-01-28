package com.microservice.apigateway.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalFallbackController {

    @GetMapping("/fallback/global")
    public Map<String, Object> globalFallback(
            ServerWebExchange exchange,
            @RequestParam(value = "error", required = false) String error) {
        String routeId = (String) exchange.getAttributes().get("routeId");
        Map<String, Object> response = new HashMap<>();
        response.put("message", routeId+" Service is currently unavailable. Please try again later.");
        response.put("status", 503); // Service Unavailable
        response.put("timestamp", System.currentTimeMillis());
        response.put("routeId", routeId != null ? routeId : "Unknown"); // Identifies the failing route
        response.put("error", error != null ? error : "Unknown error");

        return response;
    }
}
