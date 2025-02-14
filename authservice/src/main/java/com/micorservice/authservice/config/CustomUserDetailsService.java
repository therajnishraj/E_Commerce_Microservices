package com.micorservice.authservice.config;

import com.micorservice.authservice.entity.UserCredential;
import com.micorservice.authservice.repository.UserCredentialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * CustomUserDetailsService is a service class that implements {@link UserDetailsService}
 * to provide authentication details for Spring Security.
 *
 * This service fetches user credentials from the database based on the username
 * and returns a {@link UserDetails} object required for authentication.
 *
 * Key Features:
 * - Retrieves user details from the database using {@link UserCredentialRepository}.
 * - Throws an exception if the user is not found.
 * - Logs authentication requests for debugging.
 *
 * Usage:
 * - This service is used by Spring Security during authentication.
 * - It is automatically picked up due to the {@code @Component} annotation.
 *
 * @author Rajnish Raj
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserCredentialRepository repository;

    /**
     * Loads user details by username for authentication.
     *
     * @param username the username of the user trying to authenticate.
     * @return UserDetails containing authentication details.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       log.info("in loadUserByUsername");
        Optional<UserCredential> credential = repository.findByName(username);
        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }
}
