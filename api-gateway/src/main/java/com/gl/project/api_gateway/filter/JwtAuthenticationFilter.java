package com.gl.project.api_gateway.filter;


import com.gl.project.api_gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.info("Incoming request: {} {}", request.getMethod(), path);

        // Skip JWT check for open endpoints (register, login)
        if (!routeValidator.isSecured.test(request)) {
            log.info("Open endpoint accessed, skipping JWT validation: {}", path);
            return chain.filter(exchange);
        }

        // Check for Authorization header
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.warn("Missing Authorization header for path: {}", path);
            return onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header format for path: {}", path);
            return onError(exchange, "Invalid Authorization header format. Expected: Bearer <token>", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.validateToken(token)) {
                log.warn("Invalid or expired JWT token for path: {}", path);
                return onError(exchange, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
            }

            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            log.info("JWT validated successfully for user: {}, role: {}, path: {}", username, role, path);

            // Forward user info to downstream services via headers
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-Auth-User-Email", username)
                    .header("X-Auth-User-Role", role != null ? role : "")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("JWT validation failed for path: {}. Error: {}", path, e.getMessage());
            return onError(exchange, "JWT validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                "{\"success\":false,\"message\":\"%s\",\"data\":null}",
                errorMessage
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1; // Highest priority - runs before all other filters
    }
}