package com.example.stock.web.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTransactionService {

    @Resource(name = "redisTransactionRedisTemplate")
    private final RedisTemplate<String, Integer> redisTemplate;

    @Transactional
    public Object increaseDAU(String key, int incrementCount) {
        redisTemplate.opsForValue().set(key, incrementCount);

        if (incrementCount < 0) {
            log.error("DAU: {}", incrementCount);
            throw new IllegalArgumentException("DAU는 음수일 수 없습니다.");
        }

        log.info("DAU: {}", incrementCount);
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object increaseDAUwithPropagation(String key, int incrementCount) {
        redisTemplate.opsForValue().set(key, incrementCount);

        if (incrementCount < 0) {
            log.error("DAU: {}", incrementCount);
            throw new IllegalArgumentException("DAU는 음수일 수 없습니다.");
        }

        log.info("DAU: {}", incrementCount);
        return redisTemplate.opsForValue().get(key);
    }
}
