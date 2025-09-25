package com.rag_chat.chat.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rag_chat.chat.security.ApiKeyFilter;

@Configuration
public class WebConfig {
    private final ApiKeyFilter apiKeyFilter;

    public WebConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration() {
        FilterRegistrationBean<ApiKeyFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(apiKeyFilter);
        reg.addUrlPatterns("/api/*");
        reg.setOrder(1);
        return reg;
    }
}

