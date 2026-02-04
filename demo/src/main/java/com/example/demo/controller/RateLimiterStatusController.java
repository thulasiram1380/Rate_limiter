package com.example.demo.controller;

import com.example.demo.service.RateLimiterService;
import com.example.demo.util.ClientIdUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;


import java.util.Map;

@RestController
public class RateLimiterStatusController {

    private final RateLimiterService rateLimiterService;

    public RateLimiterStatusController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/rate-limiter/status")
    public Mono<ResponseEntity<Map<String, Object>>> getRateLimiterStatus(ServerWebExchange exchange) {

        String clientId = ClientIdUtil.getClientId(exchange);

        return Mono.just(ResponseEntity.ok(
                Map.of(
                        "status", "UP",
                        "service", "rate-limiting-gateway",
                        "clientId", clientId,
                        "capacity", rateLimiterService.getCapacity(clientId),
                        "availableTokens", rateLimiterService.getAvailableToken(clientId)
                )
        ));
    }

    @GetMapping("/api/test")
    public Mono<ResponseEntity<Map<String, Object>>> rateLimitedApi(ServerWebExchange exchange) {

        String clientId = ClientIdUtil.getClientId(exchange);

        boolean allowed = rateLimiterService.isAllowed(clientId);

        if (!allowed) {
            return Mono.just(
                    ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                            .body(Map.of(
                                    "status", 429,
                                    "error", "Too Many Requests",
                                    "message", "Rate limit exceeded",
                                    "clientId", clientId
                            ))
            );
        }

        return Mono.just(
                ResponseEntity.ok(
                        Map.of(
                                "status", 200,
                                "message", "Request allowed",
                                "clientId", clientId
                        )
                )
        );
    }

}
