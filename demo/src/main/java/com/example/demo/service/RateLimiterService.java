package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedisTokenBucketService redisTokenBucketService;

    public  boolean isAllowed(String ClientId){
        return redisTokenBucketService.isAllowed(ClientId);
    }
    public long getCapacity(String ClientId) {
        return redisTokenBucketService.getCapacity(ClientId);
    }
    public long getAvailableToken(String ClientId) {
        return redisTokenBucketService.getAvailableToken(ClientId);
    }
}
