package com.deliverytech.delivery_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

        private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
        private final Map<String, Long> lastResetTime = new ConcurrentHashMap<>();

        // Limit: 100 requests per minute per IP
        private static final int MAX_REQUESTS = 100;
        private static final long TIME_WINDOW = 60000; // 1 minute

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                        FilterChain filterChain)
                        throws ServletException, IOException {

                String clientIp = request.getRemoteAddr();
                long currentTime = System.currentTimeMillis();

                lastResetTime.putIfAbsent(clientIp, currentTime);
                requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));

                if (currentTime - lastResetTime.get(clientIp) > TIME_WINDOW) {
                        lastResetTime.put(clientIp, currentTime);
                        requestCounts.get(clientIp).set(0);
                }

                if (requestCounts.get(clientIp).incrementAndGet() > MAX_REQUESTS) {
                        response.setStatus(429);
                        response.getWriter().write("Too many requests");
                        return;
                }

                filterChain.doFilter(request, response);
        }
}
