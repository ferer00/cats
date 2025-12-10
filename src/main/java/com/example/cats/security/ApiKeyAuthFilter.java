package com.example.cats.security;

import com.example.cats.controller.config.ApiKeyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "X-API-KEY";

    private final ApiKeyProperties apiKeyProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String apiKey = request.getHeader(HEADER_NAME);

        if (apiKey == null || apiKey.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!apiKey.equals(apiKeyProperties.getKey())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String body = """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Invalid API Key",
                      "path": "%s"
                    }
                    """.formatted(request.getRequestURI());
            response.getWriter().write(body);
            return;
        }

        var auth = new UsernamePasswordAuthenticationToken(
                "api-key-client",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_API_CLIENT"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}

