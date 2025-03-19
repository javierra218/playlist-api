package com.quipux.playlist.config;

import com.quipux.playlist.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos
                        .requestMatchers("/auth/authenticate").permitAll()
                        .requestMatchers("/public/**").permitAll()

                        // PlaylistController
                        .requestMatchers(HttpMethod.POST, "/lists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/lists").authenticated()
                        .requestMatchers(HttpMethod.GET, "/lists/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/lists/**").hasRole("ADMIN")

                        // SongController
                        .requestMatchers(HttpMethod.POST, "/songs").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/songs").authenticated()
                        .requestMatchers(HttpMethod.GET, "/songs/artist/**").authenticated()

                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin sesión
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Codificarr de contraseñas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Gestor de autenticación
    }
}