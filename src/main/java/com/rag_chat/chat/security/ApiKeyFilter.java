package com.rag_chat.chat.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    private final Environment env;

    public ApiKeyFilter(Environment env) {
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        // Skip health and docs endpoints
        if (path.startsWith("/api/health") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        String apiKey = req.getHeader("X-API-Key");
        if (apiKey == null) {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
                apiKey = authHeader.substring(7);
            }
        }

        if (apiKey == null || !apiKey.equals(env.getProperty("app.api-key"))) {
            log.warn("Unauthorized request without valid API key to {} from {}", path, req.getRemoteAddr());
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"API key is required or invalid\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}

