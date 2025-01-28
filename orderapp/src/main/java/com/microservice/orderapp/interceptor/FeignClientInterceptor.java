package com.microservice.orderapp.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Autowired
    HttpServletRequest request;
    @Override
    public void apply(RequestTemplate template) {
        String token=request.getHeader(HttpHeaders.AUTHORIZATION);
        template.header("Authorization", token);
    }
}
