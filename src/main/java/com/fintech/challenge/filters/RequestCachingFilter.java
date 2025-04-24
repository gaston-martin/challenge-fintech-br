package com.fintech.challenge.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
public class RequestCachingFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestCachingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);

        LOGGER.info("Request URI: {}", request.getRequestURI());
        LOGGER.info("Request Method: {}", request.getMethod());
        String requestData = new String(cachedHttpServletRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        LOGGER.info("Request data: {}", requestData);
        filterChain.doFilter(cachedHttpServletRequest, response);
    }
}
