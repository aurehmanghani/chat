package com.rag_chat.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment env;

    @GetMapping
    public Object health() {
        long start = System.currentTimeMillis();
        try {
            long dbStart = System.currentTimeMillis();
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            long dbResponse = System.currentTimeMillis() - dbStart;

            return new Object() {
                public final String status = "healthy";
                public final String timestamp = java.time.OffsetDateTime.now().toString();
                public final String version = env.getProperty("app.version", "1.0.0");
                public final String environment = env.getProperty("spring.profiles.active", "development");
                public final double uptime = (double) (System.nanoTime()) / 1_000_000_000.0;
                public final Object database = new Object() {
                    public final String status = "connected";
                    public final long responseTimeMs = dbResponse;
                };
                public final long responseTimeMs = System.currentTimeMillis() - start;
            };
        } catch (Exception ex) {
            return org.springframework.http.ResponseEntity.status(503).body(new Object() {
                public final String status = "unhealthy";
                public final String timestamp = java.time.OffsetDateTime.now().toString();
                public final String error = ex.getMessage();
                public final long responseTimeMs = System.currentTimeMillis() - start;
            });
        }
    }

    @GetMapping("/ready")
    public Object ready() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return new Object() {
                public final String status = "ready";
                public final String timestamp = java.time.OffsetDateTime.now().toString();
            };
        } catch (Exception ex) {
            return org.springframework.http.ResponseEntity.status(503).body(new Object() {
                public final String status = "not ready";
                public final String error = ex.getMessage();
            });
        }
    }

    @GetMapping("/live")
    public Object live() {
        return new Object() {
            public final String status = "alive";
            public final String timestamp = java.time.OffsetDateTime.now().toString();
            public final double uptime = (double) (System.nanoTime()) / 1_000_000_000.0;
        };
    }
}
