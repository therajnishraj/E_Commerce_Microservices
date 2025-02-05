package com.microservice.apigateway.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ServerPortLogger {

    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) {
        System.out.println("🚀 API Gateway running on port: " + event.getWebServer().getPort());
    }
}
