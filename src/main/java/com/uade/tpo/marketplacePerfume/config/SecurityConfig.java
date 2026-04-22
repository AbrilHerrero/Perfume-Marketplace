package com.uade.tpo.marketplacePerfume.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/perfume/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/perfume/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.PUT, "/perfume/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/perfume/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.GET, "/sample/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/sample/**").hasAnyRole("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/sample/**").hasAnyRole("SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/sample/**").hasAnyRole("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/sample/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/address/**").hasAnyRole("ADMIN", "BUYER")
                        .requestMatchers(HttpMethod.POST, "/address/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.PUT, "/address/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.DELETE, "/address/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/order/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/order/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/order/*/cancel").authenticated()
                        .requestMatchers("/order/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/shipments/**")
                                .hasAnyRole("ADMIN", "BUYER", "SELLER")
                        .requestMatchers(HttpMethod.POST, "/shipments/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/shipments/*/address")
                                .hasAnyRole("ADMIN", "BUYER")
                        .requestMatchers(HttpMethod.PATCH, "/shipments/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.GET, "/payments/**")
                                .hasAnyRole("ADMIN", "BUYER")
                        .requestMatchers(HttpMethod.PATCH, "/payments/*/method")
                                .hasAnyRole("ADMIN", "BUYER")
                        .requestMatchers(HttpMethod.GET, "/review/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/review/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.PUT, "/review/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.DELETE, "/review/**").hasRole("BUYER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/cart/**").hasRole("BUYER")
                        .requestMatchers("/error/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
