package com.dattran.ecommerceapp.config;

import com.dattran.ecommerceapp.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/users/auth/**",
            "/api/v1/products/**",
            "/api/v1/categories",
            "/api/v1/products/product-detail/**",
            "/api/v1/products/get-top-4",
            "/api/v1/comments/all",
            "/api/v1/articles/all",
            "/api/v1/session/id",
            "/api/v1/actuator/**",
            "/api/v1/articles/all-article-category",
            "/api/v1/articles/get-article/**"
    };
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                                .anyRequest().authenticated());
        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
        return http.build();
    }
}
