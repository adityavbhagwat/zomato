package com.zomato.userservice.config;

import org.springframework.beans.factory.annotation.Value; // New import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder; // New import
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder; // New import
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow the bootstrap and health check endpoints to be called without
                        // authentication
                        .requestMatchers("/api/profiles/bootstrap", "/api/profiles/").permitAll()
                        // All other requests (including /api/profiles/{userId} for PATCH) must be
                        // authenticated
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))); // Configure as resource
                                                                                               // server
        return http.build();
    }

    // Bean to decode JWTs using the JWK Set URI from authservice
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}