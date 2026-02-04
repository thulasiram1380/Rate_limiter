package com.example.demo.filter;

import com.example.demo.service.RateLimiterService;
import com.example.demo.util.ClientIdUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class TokenBucketRateLimiterFilter
        extends AbstractGatewayFilterFactory<TokenBucketRateLimiterFilter.Config> {

    private final RateLimiterService rateLimiterService;

    public TokenBucketRateLimiterFilter(RateLimiterService rateLimiterService) {
        super(Config.class);
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String clientId = ClientIdUtil.getClientId(exchange);
            ServerHttpResponse response = exchange.getResponse();

            // ❗ BLOCK when NOT allowed
            if (!rateLimiterService.isAllowed(clientId)) {
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                addRateLimiterHeaders(response, clientId);

                String errorBody = String.format(
                        "{\"error\":\"Too Many Requests\",\"availableTokens\":%d,\"capacity\":%d}",
                        rateLimiterService.getAvailableToken(clientId),
                        rateLimiterService.getCapacity(clientId)
                );

                return response.writeWith(
                        Mono.just(
                                response.bufferFactory()
                                        .wrap(errorBody.getBytes(StandardCharsets.UTF_8))
                        )
                );
            }

            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() ->
                            addRateLimiterHeaders(response, clientId)
                    ));
        };
    }

    private void addRateLimiterHeaders(ServerHttpResponse response, String clientId) {
        response.getHeaders().add(
                "X-RateLimit-Remaining",
                String.valueOf(rateLimiterService.getAvailableToken(clientId))
        );
        response.getHeaders().add(
                "X-RateLimit-Capacity",
                String.valueOf(rateLimiterService.getCapacity(clientId))
        );
    }

    public static class Config {
    }
}
