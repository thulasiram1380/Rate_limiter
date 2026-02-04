package com.example.demo.service;

import com.example.demo.config.RateLimiterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@RequiredArgsConstructor
public class RedisTokenBucketService {

    private final RateLimiterProperties properties;
    private final JedisPool jedisPool;

    private static final String TOKENS_KEY_PREFIX = "rate_limiter:tokens:";
    private static final String LAST_REFILL_KEY_PREFIX = "rate_limiter:last_refill:";

    /**
     * Called ONLY by rate-limited APIs
     * - Refills tokens if needed
     * - Consumes 1 token
     */
    public boolean isAllowed(String clientId) {
        String tokenKey = TOKENS_KEY_PREFIX + clientId;

        try (Jedis jedis = jedisPool.getResource()) {
            refillTokens(clientId, jedis);

            String tokenStr = jedis.get(tokenKey);
            long currentTokens = tokenStr != null
                    ? Long.parseLong(tokenStr)
                    : properties.getCapacity();

            if (currentTokens <= 0) {
                return false;
            }

            jedis.decr(tokenKey);
            return true;
        }
    }

    /**
     * Read-only: returns max bucket capacity
     */
    public long getCapacity(String clientId) {
        return properties.getCapacity();
    }

    /**
     * Read-only: MUST NOT refill or mutate state
     */
    public long getAvailableToken(String clientId) {
        String tokenKey = TOKENS_KEY_PREFIX + clientId;

        try (Jedis jedis = jedisPool.getResource()) {
            String tokenStr = jedis.get(tokenKey);
            return tokenStr != null
                    ? Long.parseLong(tokenStr)
                    : properties.getCapacity();
        }
    }

    /**
     * Internal refill logic
     * Called ONLY inside isAllowed()
     */
    private void refillTokens(String clientId, Jedis jedis) {
        String tokenKey = TOKENS_KEY_PREFIX + clientId;
        String lastRefillKey = LAST_REFILL_KEY_PREFIX + clientId;

        long now = System.currentTimeMillis();
        String lastRefillStr = jedis.get(lastRefillKey);

        // First request → initialize bucket
        if (lastRefillStr == null) {
            jedis.set(tokenKey, String.valueOf(properties.getCapacity()));
            jedis.set(lastRefillKey, String.valueOf(now));
            return;
        }

        long lastRefillTime = Long.parseLong(lastRefillStr);
        long elapsedTime = now - lastRefillTime;

        if (elapsedTime <= 0) {
            return;
        }

        long tokensToAdd =
                (elapsedTime * properties.getRefillRate()) / 1000;

        if (tokensToAdd <= 0) {
            return;
        }

        String tokenStr = jedis.get(tokenKey);
        long currentTokens = tokenStr != null
                ? Long.parseLong(tokenStr)
                : properties.getCapacity();

        long newTokenCount = Math.min(
                currentTokens + tokensToAdd,
                properties.getCapacity()
        );

        jedis.set(tokenKey, String.valueOf(newTokenCount));
        jedis.set(lastRefillKey, String.valueOf(now));
    }
}
