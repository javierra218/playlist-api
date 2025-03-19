package com.quipux.playlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de seguridad
        http
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF (usando Lambda DSL)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/**").permitAll()  // Permitir acceso sin autenticación
                .anyRequest().permitAll()            // Cualquier otra solicitud es permitida
            );
        return http.build();
    }
}