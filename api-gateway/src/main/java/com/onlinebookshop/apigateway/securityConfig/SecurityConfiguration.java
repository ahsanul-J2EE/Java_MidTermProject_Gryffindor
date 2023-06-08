package com.onlinebookshop.apigateway.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers("/auth-server/register", "/auth-server/login")
                .permitAll()
                .pathMatchers("/book-service/book/all", "/book-service/book/{id}")
                .hasAnyAuthority("ADMIN", "USER")
                .pathMatchers("book-service/create", "book-service/update", "book-service/delete", "/product-app/hello")
                .hasAuthority("ADMIN")
                .pathMatchers("book-service/book/buy")
                .hasAuthority("USER")
                .anyExchange()
                .authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.REACTOR_CONTEXT)
                .build();
    }
}