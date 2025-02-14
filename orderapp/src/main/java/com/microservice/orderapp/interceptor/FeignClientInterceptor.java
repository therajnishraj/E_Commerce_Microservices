package com.microservice.orderapp.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
/**
 * FeignClientInterceptor is responsible for intercepting Feign client requests
 * and adding the Authorization token from the incoming HTTP request headers.
 *
 * This ensures that authenticated requests made to the Order Service
 * include the Authorization token when calling the User Service via Feign client.
 *
 * Usage:
 * - This interceptor is automatically applied to all Feign clients when making requests.
 * - It retrieves the Authorization token from the HttpServletRequest and adds it
 *   to the Feign request header.
 *
 * Prerequisites:
 * - Ensure that the incoming request contains the Authorization token in the header.
 * - The Feign client communicating with the User Service should expect an Authorization token.
 *
 * @author Rajnish Raj
 */@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Autowired
    HttpServletRequest request;
    @Override
    public void apply(RequestTemplate template) {
        String token=request.getHeader(HttpHeaders.AUTHORIZATION);
        template.header("Authorization", token);
    }
}
