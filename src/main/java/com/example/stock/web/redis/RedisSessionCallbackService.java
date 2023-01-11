package com.example.stock.web.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSessionCallbackService {

    @Resource(name = "redisSessionCallbackRedisTemplate")
    private final RedisTemplate<String, Integer> redisTemplate;

    @SuppressWarnings("unchecked")
    public void increaseDAU(String key, int incrementCount) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().increment((K) key);

                if (incrementCount < 0) {
                    log.error("DAU: {}", incrementCount);
                    throw new IllegalArgumentException("DAU는 음수일 수 없습니다.");
                }
                return operations.exec();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void increaseDAUWithWatch(String key, int incrementCount) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                try {
                    // 낙관적 락
                    operations.watch((K) key);

                    // 트랜잭션 시작
                    operations.multi();
                    operations.opsForValue().increment((K) key);

                    if (incrementCount < 0) {
                        log.error("DAU: {}", incrementCount);
                        throw new IllegalArgumentException("DAU는 음수일 수 없습니다.");
                    }
                    return operations.exec();
                } catch (Exception e) {
                    e.printStackTrace();
                    operations.discard();
                }
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void increaseDoubleDAUwithResult(String key, int incrementCount) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                try {
                    // 낙관적 락
                    operations.watch((K) key);

                    // 트랜잭션 시작
                    operations.multi();
                    operations.opsForValue().increment((K) key);

                    if (incrementCount < 0) {
                        log.error("DAU: {}", incrementCount);
                        throw new IllegalArgumentException("DAU는 음수일 수 없습니다.");
                    }

                    // 결과 조작
                    return List.of();
                } catch (Exception e) {
                    e.printStackTrace();
                    operations.discard();
                }
                return List.of();
            }
        });
    }
}
