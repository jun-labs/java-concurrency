package com.example.stock.common;

import com.example.stock.domain.stock.Stock;
import com.example.stock.web.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
public abstract class AbstractTestContainer {

    @Autowired
    protected StockRepository stockRepository;

    @Autowired
    @Resource(name = "redisSessionCallbackRedisTemplate")
    protected RedisTemplate<String, Integer> redisSessionCallback;

    @Autowired
    @Resource(name = "redisTransactionRedisTemplate")
    protected RedisTemplate<String, Integer> redisTransactionRedisTemplate;

    protected String key = "#key";

    @BeforeEach
    public void insert() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
        redisSessionCallback.opsForValue().set(key, 0);
        redisTransactionRedisTemplate.opsForValue().set(key, 0);
    }

    @AfterEach
    public void delete() {
        stockRepository.deleteAll();
    }
}
