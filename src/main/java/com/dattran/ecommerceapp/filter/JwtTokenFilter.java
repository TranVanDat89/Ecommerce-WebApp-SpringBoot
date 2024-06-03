package com.dattran.ecommerceapp.filter;

import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response); //enable bypass
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
    private boolean isBypassToken(@NonNull HttpServletRequest httpServletRequest) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("/%s/comments", apiPrefix), "GET"),
                Pair.of(String.format("/%s/comments/all", apiPrefix), "GET"),
                Pair.of(String.format("/%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("/%s/products", apiPrefix), "GET"),
                Pair.of(String.format("/%s/products/get-top-4", apiPrefix), "GET"),
                Pair.of(String.format("/%s/products/product-detail/**", apiPrefix), "GET"),
                Pair.of(String.format("/%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("/%s/users/auth/register", apiPrefix), "POST"),
                Pair.of(String.format("/%s/users/auth/login", apiPrefix), "POST"),
                Pair.of(String.format("/%s/articles/all", apiPrefix), "GET"),
                Pair.of(String.format("/%s/actuator/health", apiPrefix), "GET"),
                Pair.of(String.format("/%s/articles/all-article-category", apiPrefix), "GET")
                );
        String requestPath = httpServletRequest.getServletPath();
        String requestMethod = httpServletRequest.getMethod();
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (requestPath.contains("/api/v1/products/product-detail")) {
                return true;
            }
            if (requestPath.equals(bypassToken.getFirst())
                    && requestMethod.equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
