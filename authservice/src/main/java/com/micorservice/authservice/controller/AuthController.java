package com.micorservice.authservice.controller;

import com.micorservice.authservice.dto.AuthRequest;
import com.micorservice.authservice.entity.UserCredential;
import com.micorservice.authservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody UserCredential user) {
        try{
            log.info("****user created ");
            return ResponseEntity.ok(service.saveUser(user));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                return service.generateToken(authRequest.getUsername());
            } else {
                return "User is not allowed to access the request";
            }
        } catch (Exception e) {
            return "Invalid username and password..!";
        }

    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }
}
