package com.micorservice.authservice.config;

import com.micorservice.authservice.controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 * AuthConfig is the main security configuration class for the authentication service.
 *
 * This class configures:
 * - User authentication and authorization rules.
 * - Password encoding mechanism.
 * - Authentication provider and manager.
 * - Security filter chain for HTTP request authorization.
 *
 * Key Features:
 * - Disables CSRF protection (suitable for stateless REST APIs).
 * - Defines public and secured API endpoints.
 * - Uses BCrypt for password encoding.
 * - Implements a custom `UserDetailsService` for authentication.
 *
 * Usage:
 * - This configuration is automatically picked up by Spring Security.
 * - Used to enforce security policies in the authentication service.
 *
 * @author Rajnish Raj
 */
@Configuration
@EnableWebSecurity
public class AuthConfig {
    private static final Logger log = LoggerFactory.getLogger(AuthConfig.class);

    /**
     * Defines a custom UserDetailsService implementation for authentication.
     *
     * @return an instance of CustomUserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    /**
     * Configures security rules for HTTP requests.
     *
     * - Disables CSRF protection (since it's a stateless authentication service).
     * - Allows public access to authentication endpoints.
     * - Requires authentication for all other requests.
     *
     * @param http HttpSecurity object to configure security policies.
     * @return the configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("in securityFilterChain");
        http.csrf(csrf -> csrf.disable()) // Disabling CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/token", "/auth/validate","/actuator/health", "/actuator/info").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    /**
     * Defines the password encoder to be used for authentication.
     *
     * Uses BCrypt hashing to securely store and validate passwords.
     *
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider.
     *
     * - Uses `DaoAuthenticationProvider` to retrieve user details.
     * - Sets the custom `UserDetailsService` for authentication.
     * - Uses BCrypt password encoding.
     *
     * @return a configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Configures the authentication manager to handle user authentication.
     *
     * @param config AuthenticationConfiguration instance provided by Spring Security.
     * @return an AuthenticationManager instance.
     * @throws Exception if an error occurs during retrieval.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
