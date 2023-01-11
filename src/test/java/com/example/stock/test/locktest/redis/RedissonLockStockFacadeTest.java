package com.example.stock.test.locktest.redis;

import com.example.stock.domain.stock.Stock;
import com.example.stock.web.stock.facade.RedissonLockStockFacade;
import com.example.stock.web.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedissonLockStockFacadeTest {

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void insert() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void delete() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("Redisson의 락을 사용했을때, 100개의 상품을 동시에 주문하면 재고가 100개 감소된다.")
    void Redisson을_사용한_통합_테스트() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(100);

        // when
        for (int i = 1; i <= 100; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Stock stock = stockRepository.findStockByProductId(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }
}
